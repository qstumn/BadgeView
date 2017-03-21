package q.rorbin.badgeview;

import android.graphics.PointF;

import java.util.List;

/**
 * Created by chqiu on 2017/3/20.
 */

public class MathUtil {
    public static final double CIRCLE_RADIAN = 2 * Math.PI;

    public static double getTanRadian(double atan, int quadrant) {
        if (atan < 0) {
            atan += CIRCLE_RADIAN / 4;
        }
        atan += CIRCLE_RADIAN / 4 * (quadrant - 1);
        return atan;
    }

    public static double radianToAngle(double radian) {
        return 360 * (radian / CIRCLE_RADIAN);
    }

    public static int getQuadrant(PointF p, PointF center) {
        if (p.x > center.x) {
            if (p.y > center.y) {
                return 4;
            } else if (p.y < center.y) {
                return 1;
            }
        } else if (p.x < center.x) {
            if (p.y > center.y) {
                return 3;
            } else if (p.y < center.y) {
                return 2;
            }
        }
        return -1;
    }

    public static float getPointDistance(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    /**
     * this formula is designed by mabeijianxi
     * website : http://blog.csdn.net/mabeijianxi/article/details/50560361
     *
     * @param circleCenter The circle center point.
     * @param radius       The circle radius.
     * @param slopeLine    The slope of line which cross the pMiddle.
     */
    public static void getInnertangentPoints(PointF circleCenter, float radius, Double slopeLine, List<PointF> points) {
        float radian, xOffset, yOffset;
        if (slopeLine != null) {
            radian = (float) Math.atan(slopeLine);
            xOffset = (float) (Math.cos(radian) * radius);
            yOffset = (float) (Math.sin(radian) * radius);
        } else {
            xOffset = radius;
            yOffset = 0;
        }
        points.add(new PointF(circleCenter.x + xOffset, circleCenter.y + yOffset));
        points.add(new PointF(circleCenter.x - xOffset, circleCenter.y - yOffset));
    }
}
