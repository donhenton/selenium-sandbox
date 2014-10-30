package com.dhenton9000.screenshots.compare;

import java.util.HashSet;
import java.util.Set;

public class CompareResult {

    public static final int LIMIT = 2;
    private Set<Area> failedAreas = new HashSet<Area>();
    private boolean inError = false;
    private String errorMessage = null;
    private String simpleFileName;

    CompareResult() {

    }

    public Set<Area> getFailedAreas() {
        if (!removedOverlapping) {
            removeOverlapping();
        }

        return failedAreas;
    }

    private void removeOverlapping() {
        for (Area failedArea : failedAreas) {
            for (Area otherArea : failedAreas) {
				// if (otherArea == failedArea) {
                //					
                // }
            }
        }

    }

    boolean removedOverlapping = false;

    // public Set<Area> failedOutsideAreas = new HashSet<Area>();
    public boolean[][] failedPixels;

	// private int outsideTop;
    public CompareResult(int w, int h, int skipTop) {
        failedPixels = new boolean[w][h];
        // this.outsideTop = skipTop;
    }

    public void failed(int x, int y, int difference) {
        failedPixels[x][y] = true;

		// System.out.println("Fail at " + x + "," + y);
        Area newArea = new Area();
        newArea.x1 = x - LIMIT;
        newArea.x2 = x + LIMIT;
        newArea.y1 = y - LIMIT;
        newArea.y2 = y + LIMIT;

        Set<Area> areas = new HashSet<Area>();
        for (Area failedArea : failedAreas) {
            if (failedArea.x1 <= x + LIMIT && failedArea.x2 >= x - LIMIT) {
                if (failedArea.y1 <= y + LIMIT && failedArea.y2 >= y - LIMIT) {
                    areas.add(failedArea);
					// System.out.println("- Found area (" + failedArea.x1 + ","
                    // + failedArea.y1 + "-" + failedArea.x2 + ","
                    // + failedArea.y2 + ")");
                }
            }
        }

        if (areas.size() == 0) {
            if (difference <= 3) {
				// Small differences not near any remarkable difference are
                // ignored
            } else {
                failedAreas.add(newArea);
            }
        } else {
            for (Area a : areas) {
                newArea.x1 = Math.min(a.x1, newArea.x1);
                newArea.x2 = Math.max(a.x2, newArea.x2);
                newArea.y1 = Math.min(a.y1, newArea.y1);
                newArea.y2 = Math.max(a.y2, newArea.y2);
            }

            failedAreas.removeAll(areas);
            failedAreas.add(newArea);
        }
    }

    /**
     * @return the inError
     */
    public boolean isInError() {
        return inError;
    }

    /**
     * @param inError the inError to set
     */
    public void setInError(boolean inError) {
        this.inError = inError;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the simpleFileName
     */
    public String getSimpleFileName() {
        return simpleFileName;
    }

    /**
     * @param simpleFileName the simpleFileName to set
     */
    public void setSimpleFileName(String simpleFileName) {
        this.simpleFileName = simpleFileName;
    }

     
    public static class Area {

        public int x1, x2, y1, y2;

        public String toString() {
            return String.format("[%d,%d] [%d,%d]", x1, y1, x2, y2);
        }
    }

    public boolean isOk() {
        return (getFailedAreas() == null || getFailedAreas().isEmpty());
    }

}
