package helpers;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import java.io.IOException;
import java.io.InputStream;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Artist {

  public static final int WIDTH = 1280, HEIGHT = 960;

  public static void BeginSession() {
    Display.setTitle("CCG Game");
    try {
      Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
      Display.create();
    } catch (LWJGLException e) {
      e.printStackTrace();
    }

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
    glMatrixMode(GL_MODELVIEW);
    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  }

  public static boolean CheckCollision(float x1, float y1, float width1, float height1, float x2,
      float y2, float width2, float height2) {
    if (x1 + width1 > x2 && x1 < x2 + width2 && y1 + height1 > y2 && y1 < y2 + height2)
      return true;
    return false;
  }

  public static void DrawQuad(float x, float y, float width, float height) {
    glBegin(GL_QUADS);
    glVertex2f(x, y);
    glVertex2f(x + width, y);
    glVertex2f(x + width, y + height);
    glVertex2f(x, y + height);
    glEnd();
  }

  public static void DrawQuadTex(Texture tex, float x, float y, float width, float height) {
    tex.bind();
    glTranslatef(x, y, 0);
    glBegin(GL_QUADS);
    glTexCoord2f(0, 0);
    glVertex2f(0, 0);
    glTexCoord2f(1, 0);
    glVertex2f(width, 0);
    glTexCoord2f(1, 1);
    glVertex2f(width, height);
    glTexCoord2f(0, 1);
    glVertex2f(0, height);
    glEnd();
    glLoadIdentity(); // TODO: 3test out without this to see effect
  }

  // Rotated quad text
  public static void DrawQuadTexRot(Texture tex, float x, float y, float width, float height,
      float angle) {
    tex.bind();
    glTranslatef(x + width / 2, y + height / 2, 0);
    glRotatef(angle, 0, 0, 1);
    glTranslatef(-width / 2, -height / 2, 0);
    glBegin(GL_QUADS);
    glTexCoord2f(0, 0);
    glVertex2f(0, 0);
    glTexCoord2f(1, 0);
    glVertex2f(width, 0);
    glTexCoord2f(1, 1);
    glVertex2f(width, height);
    glTexCoord2f(0, 1);
    glVertex2f(0, height);
    glEnd();
    glLoadIdentity(); // TODO: 3test out without this to see effect
  }

  public static Texture LoadTexture(String path, String fileType) {
    if (fileType.isEmpty()) {
      fileType = "PNG";
    }
    Texture tex = null;

    InputStream in = ResourceLoader.getResourceAsStream(path);
    try {
      tex = TextureLoader.getTexture(fileType, in);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return tex;
  }

  public static Texture QuickLoad(String name) {
    return LoadTexture("res/" + name + ".png", "PNG");
  }

}
