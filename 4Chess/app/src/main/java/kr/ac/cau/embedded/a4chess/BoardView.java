package kr.ac.cau.embedded.a4chess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import kr.ac.cau.embedded.a4chess.chess.Board;
import kr.ac.cau.embedded.a4chess.chess.Coordinate;
import kr.ac.cau.embedded.a4chess.chess.Game;
import kr.ac.cau.embedded.a4chess.chess.pieces.Piece;
import kr.ac.cau.embedded.a4chess.device.BuzzerAlarm;
import kr.ac.cau.embedded.a4chess.device.DeviceController;
import kr.ac.cau.embedded.a4chess.device.DotPrintCurrentCondition;
import kr.ac.cau.embedded.a4chess.device.DotPrintSelectPiece;
import kr.ac.cau.embedded.a4chess.device.LcdPrintTurn;
import kr.ac.cau.embedded.a4chess.device.MotorRun;
import kr.ac.cau.embedded.a4chess.device.SsegPrintTime;

public class BoardView extends View {

    public static BoardView view;

    private final Paint boardPaint = new Paint();
    private final Paint textPaint = new Paint();

    private Coordinate selection;
    private GestureDetector doubleTapGestureDetector;

    private float scaleFactor = 1.0f;
    private float focusX = 0;
    private float focusY = 0;
    static public int procNum = 0; // Procedure Number - avoid duplication & may check whose turn now

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        new Thread(new Runnable() { @Override public void run() {
            while(true){
                try{
                    postInvalidate();
                    Thread.sleep(500);
                }
                catch (Exception e){

                }
            }
        }
        }).start();
        doubleTapGestureDetector = new GestureDetector(context, new DoubleTapListener());
        BoardView.view = this;


    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        canvas.translate(focusX * -1, focusY * -1);
//        canvas.scale(scaleFactor, scaleFactor);
        int boardSize = Board.getBoardSize();
        float cellWidth = canvas.getHeight() / (float) boardSize * scaleFactor;
        Coordinate coordinate;
        Piece piece;
        textPaint.setTextSize(cellWidth);
        float textOffset = 0.15f * cellWidth;
        boardPaint.reset();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                coordinate = new Coordinate(x, y);
                if (coordinate.isValid()) {
                    if ((x + y) % 2 == 0) boardPaint.setColor(Color.BLACK);
                    else boardPaint.setColor(Color.WHITE);
                    drawCoordinate(coordinate, canvas, cellWidth, boardPaint, boardSize);
                    if (isInEditMode()) {
                        continue;
                    }
                    piece = Board.getPiece(coordinate);
                    if (piece != null) {
                        textPaint.setColor(Game.getPlayerColor(piece.getPlayerId()));
                        canvas.drawText(piece.getString(), x * cellWidth,
                                (boardSize - y) * cellWidth - textOffset, textPaint);
                    }
                }
            }
            if (selection != null && (piece = Board.getPiece(selection)) != null) {
                boardPaint.setAlpha(128);
                boardPaint.setColor(Color.CYAN);
                canvas.drawCircle(selection.x * cellWidth + cellWidth / 2,
                        (boardSize - selection.y - 1) * cellWidth + cellWidth / 2, cellWidth / 2, boardPaint);
                textPaint.setColor(Game.getPlayerColor(piece.getPlayerId()));
                canvas.drawText(piece.getString(), selection.x * cellWidth,
                        (boardSize - selection.y) * cellWidth - 10, textPaint);
                for (Coordinate possible : piece.getPossiblePositions()) {
                    drawCoordinate(possible, canvas, cellWidth, boardPaint, boardSize);
                }
            }
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { //★★ TODO : should be called when "(info_game)" received
        doubleTapGestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            int boardSize = Board.getBoardSize();
            float eventX = event.getX();
            float eventY = event.getY();
            eventX = (eventX + focusX) / scaleFactor;
            eventY = (eventY + focusY) / scaleFactor;
            int x = (int) (eventX / getHeight() * boardSize);
            int y = boardSize - 1 - (int) (eventY / getHeight() * boardSize);
            Coordinate coordinate = new Coordinate(x, y);
            if (coordinate.isValid() && Board.getPiece(coordinate) != null &&
                    Board.getPiece(coordinate).getPlayerId().equals(Game.myPlayerId)) {
                selection = coordinate;
                invalidate();
                DotPrintSelectPiece.PrintSelectPiece(Board.getPiece(coordinate));
            } else {
                if (selection != null) { // we have a piece selected and clicked on a new position
                    if (Board.move(Game.myPlayerId, selection, coordinate)) { // gameMsg ex : "1 0 0 2 3(info_game)"

                        procNum++;
                        String gameMsg = Game.myPlayerId + "#";
                        gameMsg += Integer.toString(selection.x) + "#" + Integer.toString(selection.y) + "#";
                        gameMsg += Integer.toString(coordinate.x) + "#" + Integer.toString(coordinate.y) + "(info_game)";
                        if (MainActivity.nickName == "Player1") {
                            MainActivity.serverSend(gameMsg);
                        }
                        else {
                            MainActivity.clientSend(gameMsg);
                        }
                        selection = null;
                        invalidate();
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension((int)(width * scaleFactor), (int)(height * scaleFactor));
    }

    private void drawCoordinate(final Coordinate coordinate, final Canvas canvas, final float cellWidth, final Paint paint, int boardSize) {
        canvas.drawRect(coordinate.x * cellWidth, (boardSize - coordinate.y - 1) * cellWidth, (coordinate.x + 1) * cellWidth,
                (boardSize - coordinate.y) * cellWidth, paint);
    }

    private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            focusX = e.getX();
            focusY = e.getY();

            if(scaleFactor < 2.0f) {
                scaleFactor = 2.0f;
                invalidate();
            } else {
                scaleFactor = 1.0f;
                focusX = 0;
                focusY = 0;
                invalidate();
            }

            return true;
        }
    }
}
