package GAMES.Chess

import GAMES.Chess.Chess.startX.==
import GAMESENGINE.Drawable

import java.awt.{Color, Dimension, Font, Graphics, Image}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JPanel

object Chess {

  val drawables: Array[Array[Drawable]] = Array.ofDim[Drawable](8, 8)
  var turn = 0

  this.drawables(0)(0) = Piece("rook", "black", 0, 0)
  this.drawables(0)(1) = Piece("knight", "black", 0, 1)
  this.drawables(0)(2) = Piece("bishop", "black", 0, 2)
  this.drawables(0)(3) = Piece("queen", "black", 0, 3)
  this.drawables(0)(4) = Piece("king", "black", 0, 4)
  this.drawables(0)(5) = Piece("bishop", "black", 0, 5)
  this.drawables(0)(6) = Piece("knight", "black", 0, 6)
  this.drawables(0)(7) = Piece("rook", "black", 0, 7)
  for (i <- 0 until 8) {
    this.drawables(1)(i) = Piece("pawn", "black", 1, i)
    this.drawables(6)(i) = Piece("pawn", "white", 6, i)
  }
  this.drawables(7)(0) = Piece("rook", "white", 7, 0)
  this.drawables(7)(1) = Piece("knight", "white", 7, 1)
  this.drawables(7)(2) = Piece("bishop", "white", 7, 2)
  this.drawables(7)(3) = Piece("queen", "white", 7, 3)
  this.drawables(7)(4) = Piece("king", "white", 7, 4)
  this.drawables(7)(5) = Piece("bishop", "white", 7, 5)
  this.drawables(7)(6) = Piece("knight", "white", 7, 6)
  this.drawables(7)(7) = Piece("rook", "white", 7, 7)
  var isPromoting: Boolean = false
  var from: Position = Position(0, 0)
  var to: Position = Position(0, 0)
  var whiteKingPosition: Position = Position(7, 4)
  var blackKingPosition: Position = Position(0, 4)
  var lastMoved: Piece = null
  var lastEaten: Piece = null

  case class Position(x: Int, y: Int)


  // init dimentions
  val startY: Int = 15
  val cellDimention = 50
  val startX: Int = (800 - 8 * cellDimention) / 2

  // init colors
  val writeColor = new Color(0, 0, 0)
  val black = new Color(128, 128, 128)
  val white = new Color(255, 255, 255)


  case class Piece(name: String, side: String, i: Int, j: Int) extends Drawable {
    override var img: Image = ImageIO.read(new File(s"src/main/scala/GAMES/Chess/assets/$side/$name.png"))
      .getScaledInstance(30, 30, Image.SCALE_SMOOTH)
    override var x: Int = i
    override var y: Int = j
  }

  // graw empty board
  def draw(input:String): JPanel = {
    val alpha = Array("a", "b", "c", "d", "e", "f", "g", "h")
    val panel = new JPanel() {
      override def paint(g: Graphics): Unit = {
        super.paintComponent(g)
        g.setFont(new Font("default", Font.BOLD, 13))
        var alternateColor = true
        for (y <- 0 until 8) {
          for (x <- 0 until 8) {
            if (alternateColor) g.setColor(Color.white) //new Color(235, 235, 208)
            else g.setColor(new Color(72, 42, 40))
            g.fillRect(10 + x * 60, 15 + y * 60, 60, 60)
            g.setColor(Color.black)
            g.drawRect(10 + x * 60, 15 + y * 60, 60, 60)
            alternateColor = !alternateColor
            val p = drawables(y)(x)
            if (p != null) {
              g.drawImage(p.img, 25 + p.y * 60, 30 + p.x * 60, null)
            }
          }
          alternateColor = !alternateColor
          g.drawString((8 - y).toString, 0, 48 + y * 60)
          g.drawString((8 - y).toString, 493, 48 + y * 60)
          g.drawString(alpha(y), 35 + y * 60, 10)
          g.drawString(alpha(y), 35 + y * 60, 507)
        }
      }
    }
    panel setPreferredSize new Dimension(700, 510)
    panel
  }

  def controller(input: String): Boolean = {
    if (isValidSyntaxNorm(input)) {
      println(s"(syntax-checker) ${input} valid syntax :)")
      if (parseInput(input) && isValidMove(input)) {
        saveState(input)
        return applyMove(input) && kingIsSafe(input)
      }
      return false
    } else if (isValidSyntaxProm(input)) {
      println(s"(syntax-checker) ${input} valid syntax :)")
      return applyPromotion(input)
    }

    false
  }

  def isValidSyntaxNorm(input: String): Boolean = {
    input.matches("[a-h][1-8][a-h][1-8]") && !isPromoting
  }

  def isValidSyntaxProm(input: String): Boolean = {
    input.matches("q|r|b|n") && isPromoting
  }

  def parseInput(input: String): Boolean = {
    val alpha = Array('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
    from = Position(56 - input.charAt(1), alpha.indexOf(input.charAt(0), 0))
    to = Position(56 - input.charAt(3), alpha.indexOf(input.charAt(2), 0))
    println("from => (" + from.x + ", " + from.y
      + ") to => (" + to.x + ", " + to.y + ")")
    from.x != to.x || from.y != to.y
  }

  def isValidMove(input: String): Boolean = {
    val piece = drawables(from.x)(from.y).asInstanceOf[Piece]
    val dest = drawables(to.x)(to.y).asInstanceOf[Piece]
    //val turn = this.turn
    if (piece == null || (piece.side == "black" && this.turn % 2 == 0) || (piece.side == "white" && this.turn % 2 == 1)) {
      println("Don't move to empty space or pieces from other side")
      return false
    }
    if (dest != null && ((dest.side == "black" && this.turn % 2 == 1) || (dest.side == "white" && this.turn % 2 == 0))) {
      println("I can't kill my teammate")
      return false
    }
    piece.name match {
      case "rook" => rookMove(input)
      case "knight" => knightMove(input)
      case "bishop" => bishopMove(input)
      case "queen" => queenMove(input)
      case "king" => kingMove(input)
      case "pawn" => pawnMove(input)
    }
  }

  def rookMove(input: String): Boolean = {
    if (from.x == to.x && from.y > to.y) { //E
      for (y <- to.y + 1 until from.y) {
        if (drawables(from.x)(y) != null) return false
      }
      return true
    } else if (from.x == to.x && from.y < to.y) { //W
      for (y <- from.y + 1 until to.y) {
        if (drawables(from.x)(y) != null) return false
      }
      return true
    } else if (from.x < to.x && from.y == to.y) { //S
      for (x <- from.x + 1 until to.x) {
        if (drawables(x)(from.y) != null) return false
      }
      return true
    } else if (from.x > to.x && from.y == to.y) { //N
      for (x <- to.x + 1 until from.x) {
        if (drawables(x)(from.y) != null) return false
      }
      return true
    }
    false
  }

  def knightMove(input: String): Boolean = {
    (Math.abs(from.x - to.x) == 2 && Math.abs(from.y - to.y) == 1) ||
      (Math.abs(from.x - to.x) == 1 && Math.abs(from.y - to.y) == 2)
  }

  def bishopMove(input: String): Boolean = {
    if (Math.abs(from.x - to.x) == Math.abs(from.y - to.y)) {
      if (from.x > to.x && from.y < to.y) { //NE
        for (i <- 1 until (from.x - to.x)) {
          if (drawables(from.x - i)(from.y + i) != null) return false
        }
        return true
      } else if (from.x < to.x && from.y < to.y) { //SE
        for (i <- 1 until (to.x - from.x)) {
          if (drawables(from.x + i)(from.y + i) != null) return false
        }
        return true
      } else if (from.x > to.x && from.y > to.y) { //NW
        for (i <- 1 until (from.x - to.x)) {
          if (drawables(from.x - i)(from.y - i) != null) return false
        }
        return true
      } else if (from.x < to.x && from.y > to.y) { //SW
        for (i <- 1 until (to.x - from.x)) {
          if (drawables(from.x + i)(from.y - i) != null) return false
        }
        return true
      }
    }
    false
  }

  def queenMove(input: String): Boolean = {
    rookMove(input) || bishopMove(input)
  }

  def kingMove(input: String): Boolean = {
    Math.abs(from.x - to.x) <= 1 && Math.abs(from.y - to.y) <= 1
  }

  def pawnMove(input: String): Boolean = {
    val dest = drawables(to.x)(to.y).asInstanceOf[Piece]
    if (turn % 2 == 0) { //white's turn
      if (from.y == to.y) { //moving straight
        if (from.x == 6 && to.x == 4) { //2 steps
          if (dest == null && drawables(5)(to.y) == null) {
            return true
          }
        } else if (from.x - to.x == 1 && dest == null) { //1 step
          if (to.x == 0) isPromoting = true
          return true
        }
      } else if (from.x - to.x == 1) { //moving forward
        if (Math.abs(to.y - from.y) == 1) { //to the right or left
          if (dest != null && dest.side == "black") {
            if (to.x == 0) isPromoting = true
            return true
          }
        }
      }
    } else { //black's turn
      if (from.y == to.y) { //moving straight
        if (from.x == 1 && to.x == 3) { //2 steps
          if (dest == null && drawables(2)(to.y) == null) {
            return true
          }
        } else if (to.x - from.x == 1 && dest == null) { //1 step
          if (to.x == 7) isPromoting = true
          return true
        }
      } else if (to.x - from.x == 1) { //moving forward
        if (Math.abs(to.y - from.y) == 1) { //to the right or left
          if (dest != null && dest.side == "white") {
            if (to.x == 7) isPromoting = true
            return true
          }
        }
      }
    }
    false
  }

  def kingIsSafe(input: String): Boolean = {
    var color: String = ""
    val destPosition = to
    if (turn % 2 != 0) {
      color = "white"
      to = blackKingPosition
    } else {
      color = "black"
      to = whiteKingPosition
    }
    turn += 1
    for (x <- 0 until 8) {
      for (y <- 0 until 8) {
        val piece = drawables(x)(y).asInstanceOf[Piece]
        if (piece != null && piece.side == color) {
          from = Position(piece.x, piece.y)
          if (isValidMove(input)) {
            restoreState(input, destPosition)
            return false
          }
        }
      }
    }
    if (isPromoting) {
      turn -= 1
      to = destPosition
    }
    true
  }

  def saveState(input: String): Unit = {
    lastMoved = drawables(from.x)(from.y).asInstanceOf[Piece]
    lastEaten = drawables(to.x)(to.y).asInstanceOf[Piece]
  }

  def restoreState(input: String, position: Position): Unit = {
//    val lastMoved = lastMoved
//    val lastEaten = lastEaten
    drawables(this.lastMoved.x)(this.lastMoved.y) = lastMoved

    if (this.lastMoved.name == "king" && this.lastMoved.side == "black") {
      blackKingPosition = Position(this.lastMoved.x, this.lastMoved.y)
    } else if (this.lastMoved.name == "king" && this.lastMoved.side == "white") {
      whiteKingPosition = Position(this.lastMoved.x, this.lastMoved.y)
    }

    if (this.lastEaten == null) {
      drawables(position.x)(position.y) = null
    } else {
      drawables(lastEaten.x)(lastEaten.y) = lastEaten
    }

    if (isPromoting) isPromoting = false
    turn -= 1
  }

  def applyMove(input: String): Boolean = {
    val piece = drawables(from.x)(from.y).asInstanceOf[Piece]
    if (piece.name == "king" && piece.side == "white") whiteKingPosition = to
    else if (piece.name == "king" && piece.side == "black") blackKingPosition = to
    drawables(from.x)(from.y) = null
    drawables(to.x)(to.y) =
      Piece(piece.name, piece.side, to.x, to.y)
    true
  }

  def applyPromotion(input: String): Boolean = {
    var color: String = ""
    if (turn % 2 == 0) {
      color = "white"
    } else {
      color = "black"
    }
    input match {
      case "q" => drawables(to.x)(to.y) =
        Piece("queen", color, to.x, to.y)
      case "r" => drawables(to.x)(to.y) =
        Piece("rook", color, to.x, to.y)
      case "b" => drawables(to.x)(to.y) =
        Piece("bishop", color, to.x, to.y)
      case "n" => drawables(to.x)(to.y) =
        Piece("knight", color, to.x, to.y)
    }
    turn += 1
    isPromoting = false
    true
  }

}
