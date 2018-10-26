package helpers;

import org.lwjgl.Sys;

public class Clock {
  private static boolean paused = false;
  public static long lastFrame, totalTime;
  public static float d = 0, multiplier = 1;

  public static long getTime() {
    return Sys.getTime() * 1000 / Sys.getTimerResolution();
  }

  public static float getDelta() {
    long currentTime = getTime();
    int delta = (int) (currentTime - lastFrame);
    lastFrame = getTime();
    // System.out.println(delta * 0.01f);
    // prevents issues when moving screen
    if (delta * 0.001f > 0.05f)
      return 0.05f;
    return delta * 0.001f;
  }

  public static float Delta() {
    if (paused)
      return 0;
    else
      return d * multiplier;
  }

  public static float TotalTime() {
    return totalTime;
  }

  public static float Multiplier() {
    return multiplier;
  }

  public static void update() {
    d = getDelta();
    totalTime += d;
  }

  public static void ChangeMultiplier(int change) {
    if (!(multiplier + change < -1 && multiplier + change > 7)) {
      multiplier += change;
    }
  }

  public static void Pause() {
    paused = !paused;
  }
}
