package frc.robot.pathstuff;

import easypath.EasyPath;
import easypath.EasyPathConfig;
import easypath.FollowPath;
import easypath.Path;
import easypath.PathUtil;

public class Paths {

    public static final Path PATH1 = new Path(t ->
    /*
     * {"start":{"x":3,"y":27},"mid1":{"x":125,"y":24},"mid2":{"x":268,"y":7},"end":
     * {"x":262,"y":103}}
     */
    (381 * Math.pow(t, 2) + -84 * t + -9) / (-510 * Math.pow(t, 2) + 126 * t + 366), 302.304);

    public static final Path PATH2 = new Path(t ->
    /*
     * {"start":{"x":4,"y":38},"mid1":{"x":117,"y":39},"mid2":{"x":281,"y":27},"end"
     * :{"x":256,"y":138}}
     */
    (408 * Math.pow(t, 2) + -78 * t + 3) / (-720 * Math.pow(t, 2) + 306 * t + 339), 313.545);

}
