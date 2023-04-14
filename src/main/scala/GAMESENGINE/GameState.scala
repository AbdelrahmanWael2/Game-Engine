package GAMESENGINE

class GameState {
  var drawables: Array[Array[Drawable]] = Array()
  var input: String = ""
  var turn: Int = 0

  //representing the 2D array as a string
  override def toString: String = {
    var s = "{\n"
    for (row <- drawables) {
      s += "  {"
      for (d <- row) {
        if (d == null)
          s += "null,"
        else s += s"$d,"
      }
      s += "},\n"
    }
    s += "}"
    s
  }
}
