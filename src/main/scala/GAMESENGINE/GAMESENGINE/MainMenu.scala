package GAMESENGINE


import GAMES.Chess.Chess
import GAMES.Queens.queens
import GAMES.Connect4.connect
import GAMES.Sudoko.Sudoko
import GAMES.TicTacToe.TicTacToe

import java.awt.{Dimension, GridLayout}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._

object MainMenu {
  def main(args: Array[String]): Unit = {
    val frame = new MainMenu
    frame.setVisible(true)
  }
}

class MainMenu extends JFrame("GAMESENGINE.Game") {

  //rows, columns, h gap, v gap
  var gridLayout = new GridLayout(10,10,10,10)
  private val mainPanel = new JPanel(gridLayout)
  private val TicButton = new JButton("Tic-Tac-Toe")
  private val ConnectButton = new JButton("Connect 4")
  private val CheckersButton = new JButton("Checkers")
  private val ChessButton = new JButton("Chess")
  private val SudokoButton = new JButton("Sudoku")
  private val QueensButton = new JButton("8-Queens")

  def showMainMenu(): Unit = {
    this.setVisible(true)
  }



  mainPanel setBorder BorderFactory.createEmptyBorder(10, 10, 10, 10)
  mainPanel add TicButton;
  mainPanel add ConnectButton
  mainPanel add CheckersButton;
  mainPanel add ChessButton
  mainPanel add SudokoButton
  mainPanel add QueensButton

  setResizable(false)
  setMinimumSize(new Dimension(800, 800))

  setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
  setContentPane(mainPanel)
  pack()

  val ticListener: ActionListener = new ActionListener {
    def actionPerformed(e: ActionEvent): Unit = {
      new Game("TicTacToe", TicTacToe.controller, TicTacToe.draw)
      setVisible(false)
    }
  }
  
  val connectListener: ActionListener = new ActionListener {
    def actionPerformed(e: ActionEvent): Unit = {
      new Game(
        "connect 4",
        connect.controller,
        connect.draw
      )
      setVisible(false)
    }
  }

  val queensListener: ActionListener = new ActionListener {
    def actionPerformed(e: ActionEvent): Unit = {
      new Game(
        "queens",
        queens.controller,
        queens.draw
      )
      setVisible(false)
    }
  }
  val chessListener: ActionListener = new ActionListener {
    def actionPerformed(e: ActionEvent): Unit = {
      new Game(
        "chess",
        Chess.controller,
        Chess.draw
      )
      setVisible(false)
    }
  }
  val sudokoListener: ActionListener = new ActionListener {
    def actionPerformed(e: ActionEvent): Unit = {
      new Game(
        "sudoko",
        Sudoko.controller,
        Sudoko.draw
      )
      setVisible(false)
    }
  }

  TicButton.addActionListener(ticListener)
  ConnectButton.addActionListener(connectListener)
  QueensButton.addActionListener(queensListener)
  ChessButton.addActionListener(chessListener)
  SudokoButton.addActionListener(sudokoListener)
}
