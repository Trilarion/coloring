package de.frype.ui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by Jan on 02.08.2016.
 */
public class UniversalGestureDetector {

    /**
     *
     */
    public interface OnUniversalGestureListener {

        /**
         *
         * @param detector
         * @return
         */
        public boolean onDown(UniversalGestureDetector detector);

        /**
         *
         * @param detector
         * @return
         */
        public boolean onSingleTapUp(UniversalGestureDetector detector);

        /**
         *
         * @param detector
         */
        public void onLongPress(UniversalGestureDetector detector);

        /**
         *
         * @param detector
         * @return
         */
        public boolean onSingleTapConfirmed(UniversalGestureDetector detector);

        /**
         *
         * @param detector
         * @return
         */
        public boolean onDoubleTap(UniversalGestureDetector detector);

        /**
         *
         * @param detector
         * @return
         */
        public boolean onDrag(UniversalGestureDetector detector);

        /**
         *
         * @param detector
         * @return
         */
        public boolean onPinch(UniversalGestureDetector detector);

        /**
         *
         * @param detector
         */
        public void onPinchEnd(UniversalGestureDetector detector);
    }

    private final Context context;
    private final OnUniversalGestureListener listener;

    private MotionEvent previousUpEvent;

    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();

    /**
     *
     * @param context
     * @param listener
     */
    public UniversalGestureDetector(Context context, OnUniversalGestureListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /**
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();

        boolean handled = false;

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :

                if (previousUpEvent != null)

                // fire onDown
                handled |= listener.onDown(this);

                break;
            case (MotionEvent.ACTION_MOVE) :

                // fire onDraw
                handled |= listener.onDrag(this);

                break;
            case (MotionEvent.ACTION_UP) :

                // update previous up event
                previousUpEvent = event;

            case (MotionEvent.ACTION_CANCEL) :
                // cancel
            case (MotionEvent.ACTION_OUTSIDE) :
        }

        return handled;
    }

}
