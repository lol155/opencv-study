public class RangePosition {
    private Position startPosition;
    private Position endPosition;

    private Position getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Position endPosition) {
        this.endPosition = endPosition;
    }

    private Position getStartPosition() {

        return startPosition;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    public RangePosition compareStartAndSet(PositionType positionType, int val){
        return positionType.compareAndSet(this, val);
    }

    public enum PositionType{
        START_X {
            RangePosition compareAndSet(RangePosition rangePosition, int val) {
                if (val < rangePosition.getStartPosition().getRowNum()) {
                    rangePosition.getStartPosition().setRowNum(val);
                }
                return rangePosition;
            }
        },END_X {
            RangePosition compareAndSet(RangePosition rangePosition, int val) {
                if (val > rangePosition.getEndPosition().getRowNum()) {
                    rangePosition.getEndPosition().setRowNum(val);
                }
                return rangePosition;
            }
        },START_Y {
            RangePosition compareAndSet(RangePosition rangePosition, int val) {
                if (val < rangePosition.getStartPosition().getColNum()) {
                    rangePosition.getStartPosition().setColNum(val);
                }
                return rangePosition;
            }
        },END_Y {
            RangePosition compareAndSet(RangePosition rangePosition, int val) {
                if (val > rangePosition.getEndPosition().getColNum()) {
                    rangePosition.getEndPosition().setColNum(val);
                }
                return rangePosition;
            }
        };

        abstract RangePosition compareAndSet(RangePosition rangePosition,int val);
    }
}
