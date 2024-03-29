package GAMESENGINE

import com.sun.java.accessibility.util.AWTEventMonitor.addActionListener

import java.awt.Color
import java.awt.{BorderLayout, Dimension, FlowLayout, Font}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._
import javax.swing.{BorderFactory, JFrame, JLabel, JPanel, SwingConstants}

class Game(
            val name: String,
            val controller: String => Boolean,
            val draw: String => JPanel
          ) extends JFrame(name) {
  // Initializing Swing Components
  private val turnLabel = new JLabel("Player 1 Turn", SwingConstants.CENTER)
  private val mainPanel = new JPanel()
  private val layoutManager = new BorderLayout()
  private val gamePanel = draw(input)
  private val statusMsg = new JLabel("", SwingConstants.LEFT)
  private val inputField = new JTextField()
  private val submitButton = new JButton("Submit")
  private val exitButton = new JButton("Exit")
  val exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT))
  exitPanel.add(exitButton)
  val centerPanel = new JPanel(new BorderLayout())


  // Add the exit panel to the PAGE_START area of mainPanel
  mainPanel.add(exitPanel, BorderLayout.PAGE_START)

  // Setting main panel options
  setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  setContentPane(mainPanel)
  mainPanel.setLayout(layoutManager)
  mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20))

  inputField.setPreferredSize(new Dimension(250, 25))
  turnLabel.setFont(new Font("default", Font.BOLD, 20))
  statusMsg.setFont(new Font("default", Font.BOLD, 20))



  private val lowerPanel = new JPanel()
  lowerPanel.setLayout(new FlowLayout(FlowLayout.CENTER))
  lowerPanel.add(statusMsg)
  lowerPanel.add(inputField)
  lowerPanel.add(submitButton)

  lowerPanel.add(exitButton)

  private val middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER))
  middlePanel.add(gamePanel)

  mainPanel.add(turnLabel, BorderLayout.PAGE_START)
  mainPanel.add(middlePanel, BorderLayout.CENTER)
  mainPanel.add(lowerPanel, BorderLayout.PAGE_END)

  pack()
  setVisible(true)
  setResizable(false)
  var input = ""
  var turn = 0

  val e: ActionListener = new ActionListener {
    override def actionPerformed(event: ActionEvent): Unit = {
      input = inputField.getText
      update()
    }
  }

  exitButton.addActionListener(new ActionListener() {
    override def actionPerformed(e: ActionEvent): Unit = {
      dispose()
      val mainMenu = new MainMenu
      mainMenu.showMainMenu()

    }
  })


  submitButton.addActionListener(e)
  inputField.addActionListener(e)

  def update(): Unit = {
    if (controller(input)) {
      statusMsg.setText("")
      gamePanel.repaint()
      turn += 1

      if (turn % 2 == 0) {
        turnLabel.setText("Player 1 Turn")
      }
      else {
        turnLabel.setText("Player 2 Turn")
        statusMsg.setForeground(Color.GREEN)
        statusMsg.setText("Valid Move")
      }
    }else {
            statusMsg.setForeground(Color.RED)
            statusMsg.setText("Invalid Move")
          }

    inputField.setText("")
  }
}
