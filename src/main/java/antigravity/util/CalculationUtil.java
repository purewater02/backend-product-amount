package antigravity.util;

public final class CalculationUtil {
  private CalculationUtil() {
    throw new IllegalStateException("유틸 클래스 입니다.");
  }

  public static int truncateToThousand(int price) {
    return (price / 1000) * 1000;
  }
}
