package data;

import static helpers.Artist.DrawQuadTex;
import static helpers.Clock.Delta;
import java.util.ArrayList;
import org.newdawn.slick.opengl.Texture;

public class Enemy {
  private int width, height, health, currentCheckpoint;
  private float x, y, speed;
  private Texture texture;
  private Tile startTile;
  private TileGrid grid;
  private boolean first = true, alive = true;

  private ArrayList<Checkpoint> checkpoints;
  private int[] directions;

  Enemy(Texture texture, Tile startTile, TileGrid grid, int width, int height, float speed) {
    this.texture = texture;
    this.startTile = startTile;
    this.grid = grid;
    this.x = startTile.getX();
    this.y = startTile.getY();
    this.width = width;
    this.height = height;
    this.speed = speed;
    this.checkpoints = new ArrayList<Checkpoint>();
    this.directions = new int[2];
    // X Direction
    this.directions[0] = 0;
    // Y Direction
    this.directions[1] = 0;
    this.directions = FindNextD(startTile);
    this.currentCheckpoint = 0;
    PopulateCheckpointList();
  }

  public void Update() {
    if (first)
      first = false;
    else {
      if (CheckpointReached()) {
        if (currentCheckpoint + 1 == checkpoints.size()) {
          Die();
          // TODO remove enemy from map
          // TODO subtract one life from player
          System.out.println("Enemy Reached End of Maze");
        } else
          currentCheckpoint++;
      } else {
        x += Delta() * checkpoints.get(currentCheckpoint).getxDirection() * speed;
        y += Delta() * checkpoints.get(currentCheckpoint).getyDirection() * speed;
      }
    }
  }

  private boolean CheckpointReached() {
    boolean reached = false;
    Tile t = checkpoints.get(currentCheckpoint).getTile();
    // Check if position reached tile within variance of 3 (arbitrary)
    if (x > t.getX() - 3 && x < t.getX() + 3 && y > t.getY() - 3 && y < t.getY() + 3) {

      reached = true;
      x = t.getX();
      y = t.getY();
    }

    return reached;
  }

  private void PopulateCheckpointList() {
    checkpoints.add(FindNextC(startTile, directions = FindNextD(startTile)));

    int counter = 0;
    boolean cont = true;
    while (cont) {
      int[] currentD = FindNextD(checkpoints.get(counter).getTile());
      // Check if the next direction/checkpoint exists; end after 20 checkpoints (arbitrary)
      if (currentD[0] == 2 || counter == 20) {
        cont = false;
      } else {
        checkpoints.add(FindNextC(checkpoints.get(counter).getTile(),
            directions = FindNextD(checkpoints.get(counter).getTile())));
      }
      counter++;
    }
  }

  // TODO rename to FindNextCheckpoint
  private Checkpoint FindNextC(Tile s, int[] dir) {
    Tile next = null;
    Checkpoint c = null;

    // Boolean to determine if next checkpoint has been found
    boolean found = false;
    int counter = 1;
    while (!found) {
      if (s.getXPlace() + dir[0] * counter == grid.getTilesWide()
          || s.getYPlace() + dir[0] * counter == grid.getTilesHigh()
          || s.getType() != grid
              .GetTile(s.getXPlace() + dir[0] * counter, s.getYPlace() + dir[1] * counter)
              .getType()) {
        found = true;
        // Move counter back 1 to find tile before new tiletype
        counter--;
        next = grid.GetTile(s.getXPlace() + dir[0] * counter, s.getYPlace() + dir[1] * counter);
      }
      counter++;
    }
    c = new Checkpoint(next, dir[0], dir[1]);
    return c;
  }

  // TODO rename to FindNextDirection
  private int[] FindNextD(Tile s) {
    int[] dir = new int[2];
    Tile up = grid.GetTile(s.getXPlace(), s.getYPlace() - 1);
    Tile right = grid.GetTile(s.getXPlace() + 1, s.getYPlace());
    Tile down = grid.GetTile(s.getXPlace(), s.getYPlace() + 1);
    Tile left = grid.GetTile(s.getXPlace() - 1, s.getYPlace());

    if (s.getType() == up.getType() && directions[1] != 1) {
      dir[0] = 0;
      dir[1] = -1;
    } else if (s.getType() == right.getType() && directions[0] != -1) {
      dir[0] = 1;
      dir[1] = 0;
    } else if (s.getType() == down.getType() && directions[1] != -1) {
      dir[0] = 0;
      dir[1] = 1;
    } else if (s.getType() == left.getType() && directions[0] != 1) {
      dir[0] = -1;
      dir[1] = 0;
    } else {
      // NO DIRECTIONS FOUND: end of line
      dir[0] = 2;
      dir[1] = 2;
    }
    return dir;
  }

  private void Die() {
    alive = false;
  }

  public void Draw() {
    DrawQuadTex(texture, x, y, width, height);
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getSpeed() {
    return speed;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }

  public Texture getTexture() {
    return texture;
  }

  public void setTexture(Texture texture) {
    this.texture = texture;
  }

  public Tile getStartTile() {
    return startTile;
  }

  public void setStartTile(Tile startTile) {
    this.startTile = startTile;
  }

  public boolean isFirst() {
    return first;
  }

  public void setFirst(boolean first) {
    this.first = first;
  }

  public TileGrid getGrid() {
    return grid;
  }

  public void setGrid(TileGrid grid) {
    this.grid = grid;
  }

  public boolean isAlive() {
    return alive;
  }
}
