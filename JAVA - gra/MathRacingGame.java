import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;


public class MathRacingGame extends JFrame {

    private int unlockedLevels = 1;
    private final Color[] levelColors = {
            new Color(255, 0, 0),
            new Color(0, 255, 0),
            new Color(255, 20, 147),
            new Color(0, 255, 255),
            new Color(255, 165, 0),
            new Color(255, 192, 203),
            new Color(0, 255, 255),
            new Color(0, 100, 255),
            new Color(255, 192, 203),
            new Color(255, 20, 147),
            new Color(255, 215, 0),
            new Color(2, 205, 190),
            new Color(255, 192, 203),
            new Color(0, 255, 0),
            new Color(0, 255, 255),
            new Color(255, 165, 0),
            new Color(255, 20, 147),
            new Color(0, 100, 255),
            new Color(2, 205, 190),
            new Color(255, 215, 0)
    };
    private JPanel levelsPanel;

    private JComboBox usersComboBox;

    private User user = new User("GOŚĆ");

    private final int[] bestTimes = new int[20]; // Przechowujemy najlepszy czas dla każdego poziomu
    private final int[] stars = new int[20]; // Tablica na liczbę gwiazdek dla każdego poziomu

    public MathRacingGame() {
        setTitle("MATEMATYCZNY WYŚCIG");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tworzymy etykietę tytułową
        JLabel titleLabel = new JLabel("MATEMATYCZNY WYŚCIG", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Chalkboard", Font.BOLD, 70));
        titleLabel.setOpaque(true);
        titleLabel.setBorder(new EmptyBorder(20, 10, 0, 10));
        titleLabel.setBackground(new Color(245, 243, 159));
        titleLabel.setForeground(new Color(100, 200, 100));
        add(titleLabel, BorderLayout.NORTH);

        Set<String> usernames = loadUniqueUsernames("users.txt");

        //JComboBox z nazwami użytkowników
        usersComboBox = new JComboBox<>();
        usersComboBox.addItem("GOŚĆ"); // Opcja domyślna

        for (String username : new TreeSet<>(usernames)) { // Sortowanie alfabetyczne
            usersComboBox.addItem(username);
        }
        usersComboBox.addActionListener(e -> gameLoad());
        add(usersComboBox, BorderLayout.SOUTH);

        // Tworzymy pasek menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Menu");

        JMenuItem newUserItem = new JMenuItem("Nowy użytkownik");
        newUserItem.addActionListener(e -> setNewUserName());
        fileMenu.add(newUserItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Panel na przyciski
        levelsPanel = new JPanel();
        levelsPanel.setLayout(new GridLayout(4, 5, 20, 20)); // 4 rzędy, 5 kolumn
        levelsPanel.setBackground(new Color(245, 243, 159));
        levelsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


        Random random = new Random(); // Generator losowych liczb
        // Tworzymy 20 przycisków
        for (int i = 1; i <= 20; i++) {
            JButton levelButton = new JButton("POZIOM " + i);
            levelButton.setFont(new Font("Britannic", Font.PLAIN, 18));

            Color assignedColor = levelColors[i - 1];

            if (i <= unlockedLevels) {
                // Poziom jest aktywny
                levelButton.setBackground(assignedColor);
                levelButton.setForeground(Color.BLACK);
                levelButton.setBorder(BorderFactory.createLineBorder(assignedColor.darker(), 2, true));
                int finalI = i;
                levelButton.addActionListener((ActionEvent e) -> uruchomPoziom(finalI)); // Obsługa kliknięcia
                levelButton.setOpaque(true);
            } else {
                // Poziom jest zablokowany
                levelButton.setEnabled(false); // Blokujemy przycisk
                // Zmniejszamy nasycenie koloru (szary odcień)
                Color desaturatedColor = desaturateColor(assignedColor, 0.3f);
                levelButton.setBackground(desaturatedColor);
                levelButton.setForeground(Color.BLACK);
                levelButton.setBorder(BorderFactory.createLineBorder(desaturatedColor.darker(), 2, true));
                levelButton.setText("🔒 POZIOM " + i); // Dodajemy symbol kłódki
                levelButton.setOpaque(true);
            }

            levelsPanel.add(levelButton);
        }

        add(levelsPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Metoda do zmniejszania nasycenia koloru
    private Color desaturateColor(Color color, float factor) {
        float[] hsbValues = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsbValues[1] *= factor; // Zmniejszamy nasycenie
        return Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2]);
    }

    private void gameLoad() {
        String selectedUser = (String) usersComboBox.getSelectedItem();

        if ("GOŚĆ".equals(selectedUser)) {
            resetToGuest(); // Resetujemy dane do wartości początkowych dla "GOŚĆ"
            return;
        }

        String userLine = findUser("users.txt", selectedUser);

        if (userLine != null) {
            // Format w pliku: nazwa, odblokowanePoziomy, czas1, czas2, ...
            String[] parts = userLine.split(",");
            user.username = parts[0];
            user.unlockedLevels = Integer.parseInt(parts[1]);

            // Odczytujemy czasy najlepsze
            for (int i = 2; i < 22; i++) {
                user.bestTimes[i - 2] = Integer.parseInt(parts[i]);
            }
            int k = 0;
            for (int i = 22; i < parts.length; i++) {
                user.stars[k] = Integer.parseInt(parts[i]);
                k+=1;
            }

            // Ustawiamy stan gry
            unlockedLevels = user.unlockedLevels;
            System.arraycopy(user.bestTimes, 0, bestTimes, 0, bestTimes.length);
            System.arraycopy(user.stars, 0, stars, 0, stars.length);

            updateLevelButtons();

            updateLevelButtons();
        } else {
            JOptionPane.showMessageDialog(this, "Nie znaleziono danych dla użytkownika: " + selectedUser,
                    "Informacja", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Metoda ustawiająca początkową grę
    private void resetToGuest() {
        user = new User("GOŚĆ");
        unlockedLevels = 1;
        Arrays.fill(bestTimes, 0);
        updateLevelButtons();
    }

    // Metoda szukająca danego użytkownika w pliku txt
    private String findUser(String filePath, String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ",")) {
                    return line;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas odczytu pliku: " + e.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return null; // Jeśli nic nie znaleziono
    }


    // Dodanie nowego użytkownika
    private void setNewUserName() {
        // Okno dialogowe do wprowadzenia nazwy użytkownika
        String newUserName = JOptionPane.showInputDialog(this,
                "Podaj nazwę użytkownika:", "Nowy użytkownik", JOptionPane.PLAIN_MESSAGE);

        // Jeśli użytkownik wprowadził nazwę, aktualizujemy
        if (newUserName != null && !newUserName.trim().isEmpty()) {
            user = new User(newUserName);

            // Dodajemy nowego użytkownika do JComboBox
            usersComboBox.addItem(user.username);

            // Ustawiamy nowego użytkownika jako wybranego
            usersComboBox.setSelectedItem(user.username);
            gameLoad();
        }
    }

    // Metoda uruchamiania poziomu
    private void uruchomPoziom(int poziom) {
        String[] equations = generateEquationsForLevel(poziom, 10); // Generujemy 10 zadań

        JFrame equationsFrame = new JFrame("Poziom " + poziom);
        equationsFrame.setSize(600, 600);
        equationsFrame.setLayout(new BorderLayout());

        JPanel equationsPanel = new JPanel();
        equationsPanel.setLayout(new GridLayout(10, 3, 10, 10));

        JTextField[] answerFields = new JTextField[10];
        JLabel[] feedbackLabels = new JLabel[10];

        for (int i = 0; i < 10; i++) {
            JLabel equationLabel = new JLabel(equations[i] + " =");
            equationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            JTextField answerField = new JTextField();
            JLabel feedbackLabel = new JLabel();
            feedbackLabel.setHorizontalAlignment(SwingConstants.LEFT);

            equationsPanel.add(equationLabel);
            equationsPanel.add(answerField);
            equationsPanel.add(feedbackLabel);

            answerFields[i] = answerField;
            feedbackLabels[i] = feedbackLabel;
        }

        // Limit czasu zależny od poziomu
        int timeLimit = getTimeLimitForLevel(poziom); // W sekundach
        JLabel timerLabel = new JLabel("Czas: " + timeLimit + "s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.RED);

        Timer timer = new Timer(1000, new ActionListener() {
            int timeRemaining = timeLimit;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeRemaining > 0) {
                    timeRemaining--;
                    timerLabel.setText("Czas: " + timeRemaining + "s");
                } else {
                    ((Timer) e.getSource()).stop(); // Zatrzymujemy odliczanie
                    JOptionPane.showMessageDialog(equationsFrame,
                            "Czas minął! Spróbuj ponownie.",
                            "Limit czasu", JOptionPane.WARNING_MESSAGE);
                    equationsFrame.dispose();
                    uruchomPoziom(poziom); // Restart poziomu
                }
            }
        });

        // Dodanie WindowListener do equationsFrame
        equationsFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                timer.stop(); // Zatrzymujemy timer przy zamykaniu okna
            }
        });

        timer.start();



        long startTime = System.currentTimeMillis();

        JButton submitButton = new JButton("Koniec");
        submitButton.addActionListener(e -> {
            timer.stop(); // Zatrzymujemy timer po kliknięciu
            int correctAnswers = 0;

            for (int i = 0; i < 10; i++) {
                String userAnswerStr = answerFields[i].getText();
                try {
                    int userAnswer = Integer.parseInt(userAnswerStr);
                    int correctAnswer = evaluateEquation(equations[i]);

                    if (userAnswer == correctAnswer) {
                        feedbackLabels[i].setText("✔");
                        feedbackLabels[i].setForeground(Color.GREEN);
                        correctAnswers++;
                    } else {
                        feedbackLabels[i].setText("✘ (" + correctAnswer + ")");
                        feedbackLabels[i].setForeground(Color.RED);
                    }
                } catch (NumberFormatException ex) {
                    feedbackLabels[i].setText("✘ (Błędny format)");
                    feedbackLabels[i].setForeground(Color.RED);
                }
            }

            int elapsedTime = (int) ((System.currentTimeMillis() - startTime) / 1000); // Czas w sekundach
            int starsAwarded = calculateStars(timeLimit, elapsedTime);

            if (stars[poziom - 1] < starsAwarded) {
                stars[poziom - 1] = starsAwarded; // Aktualizujemy najlepszą liczbe gwiazdek
            }

            if (correctAnswers == 10) {
                if (poziom == 20) {
                    // Aktualizacja najlepszego czasu
                    if (bestTimes[poziom - 1] == 0 || elapsedTime < bestTimes[poziom - 1]) {
                        bestTimes[poziom - 1] = elapsedTime;
                    }
                    user.bestTimes = bestTimes;

                    JOptionPane.showMessageDialog(equationsFrame,
                            "Gratulacje! UKOŃCZONO WSZYSTKIE POZIOMY!!!\n" +
                                    "JESTEŚ MISTRZEM LICZENIA! \n" +
                                    "Twój czas: " + elapsedTime + "s\n" +
                                    "Najlepszy czas: " + bestTimes[poziom - 1] + "s\n" +
                                    "Zdobyte gwiazdki: " + starsAwarded + "/3" + "\n" +
                                    "Najlepszy wynik gwiazdek: " + stars[poziom - 1] + "/3",
                            "Wynik", JOptionPane.INFORMATION_MESSAGE);
                    equationsFrame.dispose();
                    updateLevelButtons(); // Aktualizujemy przyciski poziomów
                    saveUserToFile();
                } else if (poziom == unlockedLevels) {
                    // Aktualizacja najlepszego czasu
                    if (bestTimes[poziom - 1] == 0 || elapsedTime < bestTimes[poziom - 1]) {
                        bestTimes[poziom - 1] = elapsedTime;
                    }
                    user.bestTimes = bestTimes;

                    JOptionPane.showMessageDialog(equationsFrame,
                            "Gratulacje! Odblokowano kolejny poziom!\n" +
                                    "Twój czas: " + elapsedTime + "s\n" +
                                    "Najlepszy czas: " + bestTimes[poziom - 1] + "s\n" +
                                    "Zdobyte gwiazdki: " + starsAwarded + "/3" + "\n" +
                                    "Najlepszy wynik gwiazdek: " + stars[poziom - 1] + "/3",
                            "Wynik", JOptionPane.INFORMATION_MESSAGE);
                    unlockedLevels = Math.max(unlockedLevels, poziom + 1);
                    equationsFrame.dispose();
                    updateLevelButtons(); // Aktualizujemy przyciski poziomów
                    user.unlockedLevels += 1; // Aktualizujemy pola użytkownika
                    saveUserToFile();
                } else {
                    if (bestTimes[poziom - 1] == 0 || elapsedTime < bestTimes[poziom - 1]) {
                        bestTimes[poziom - 1] = elapsedTime;
                    }
                    user.bestTimes = bestTimes;

                    JOptionPane.showMessageDialog(equationsFrame,
                            "Gratulacje!\n" +
                                    "Twój czas: " + elapsedTime + "s\n" +
                                    "Najlepszy czas: " + bestTimes[poziom - 1] + "s\n" +
                                    "Zdobyte gwiazdki: " + starsAwarded + "/3" + "\n" +
                                    "Najlepszy wynik gwiazdek: " + stars[poziom - 1] + "/3",
                            "Wynik", JOptionPane.INFORMATION_MESSAGE);
                    unlockedLevels = Math.max(unlockedLevels, poziom + 1);
                    equationsFrame.dispose();
                    updateLevelButtons(); // Aktualizujemy przyciski poziomów
                    saveUserToFile();
                }
            } else {
                JOptionPane.showMessageDialog(equationsFrame,
                        "Poprawne odpowiedzi: " + correctAnswers + "/10\nSpróbuj ponownie.",
                        "Wynik", JOptionPane.INFORMATION_MESSAGE);
                equationsFrame.dispose();
                uruchomPoziom(poziom); // Restart poziomu
            }
        });

        equationsFrame.add(timerLabel, BorderLayout.NORTH);
        equationsFrame.add(new JScrollPane(equationsPanel), BorderLayout.CENTER);
        equationsFrame.add(submitButton, BorderLayout.SOUTH);

        equationsFrame.setVisible(true);
    }

    // Funkcja do obliczania liczby gwiazdek na podstawie czasu
    private int calculateStars(int timeLimit, int elapsedTime) {
        if (elapsedTime <= (timeLimit * 6.5) / 10) {
            return 3; // Maksymalna liczba gwiazdek
        } else if (elapsedTime <= (timeLimit * 8.5) / 10) {
            return 2; // Średnia liczba gwiazdek
        } else {
            return 1; // Minimalna liczba gwiazdek
        }
    }



    // Funkcja ustalająca limit czasu dla poziomu
    private int getTimeLimitForLevel(int level) {
        if (level <= 5) {
            return 100;
        } else if (level <= 10) {
            return 160;
        } else if (level <= 15) {
            return 180;
        } else {
            return 220;
        }
    }


    // Funkcja do generowania równań matematycznych dla danego poziomu
    public static String[] generateEquationsForLevel(int level, int count) {
        String[] equations = new String[count];
        Random random = new Random();

        // Ustalamy zakres liczby operandów w zależności od poziomu
        int minOperands, maxOperands;

        if (level <= 3) {
            minOperands = 2;
            maxOperands = 3; // Poziom 1-3: 2-3 operandy
        } else if (level <= 5) {
            minOperands = 2;
            maxOperands = 4;
        } else if (level <= 8) {
            minOperands = 2;
            maxOperands = 3;
        } else if (level <= 10) {
            minOperands = 2;
            maxOperands = 4;
        } else if (level <= 13) {
            minOperands = 2;
            maxOperands = 3;
        } else {
            minOperands = 2;
            maxOperands = 5;
        }

        // Generowanie równań dla każdego poziomu
        for (int i = 0; i < count; i++) {
            int numberOfOperands = random.nextInt(maxOperands - minOperands + 1) + minOperands; // Losujemy liczbę operandów w określonym zakresie
            StringBuilder equation = new StringBuilder();

            int previousNumber = 0; // Przechowuje ostatnią wygenerowaną liczbę
            boolean divisionUsed = false; // Flaga, która śledzi, czy dzielenie zostało już użyte

            for (int j = 0; j < numberOfOperands; j++) {
                int num;

                // Generowanie liczby na podstawie poziomu
                if (level <= 2) {
                    num = random.nextInt(30) + 1;
                } else if (level <= 5) {
                    num = random.nextInt(100) + 1;
                } else if (level <= 7) {
                    num = random.nextInt(30) + 1;
                } else if (level <= 10) {
                    num = random.nextInt(50) + 1;
                } else if (level <= 12) {
                    num = random.nextInt(100) + 1;
                } else if (level <= 15) {
                    num = random.nextInt(100) + 1;
                } else {
                    num = random.nextInt(50) + 1;
                }

                // Jeśli to nie pierwszy operand, dodaj operator
                if (j > 0) {
                    char operator;

                    if (level <= 5) {
                        operator = random.nextBoolean() ? '+' : '-';
                    } else if (level <= 10) {
                        operator = getRandomOperator(random, '+', '-', '*');
                    } else if (level <= 15) {
                        operator = getRandomOperator(random, '+', '-', '/');
                    } else {
                        operator = getRandomOperator(random, '+', '-', '*', '/');
                    }

                    // Jeśli jeszcze nie użyto dzielenia, możemy je dodać, ale tylko raz
                    if (operator == '/' && !divisionUsed) {
                        // Losujemy dzielnik dla previousNumber (aby było możliwe podzielenie)
                        num = getRandomDivisor(previousNumber, random);
                        divisionUsed = true; // Ustawiamy flagę, że dzielenie zostało użyte
                    } else if (operator == '/') {
                        // Jeśli dzielenie już występuje, losujemy inny operator
                        operator = getRandomOperator(random, '+', '-', '*');
                    }

                    equation.append(" ").append(operator).append(" ");
                }

                equation.append(num);
                previousNumber = num; // Ustawiamy poprzednią liczbę na obecną
            }

            equations[i] = equation.toString();
        }

        return equations;
    }

    // Metoda losująca dzielnik liczby
    private static int getRandomDivisor ( int number, Random random) {
        if (number == 0) return 1; // Zapewniamy bezpieczeństwo dla dzielenia przez 0
        ArrayList<Integer> divisors = new ArrayList<>();

        for (int i = 2; i <= Math.abs(number); i++) {
            if (number % i == 0) {
                divisors.add(i);
            }
        }
        if (divisors.size() == 1) {
            return divisors.get(random.nextInt(divisors.size()));
        } else {
            return divisors.get(random.nextInt(divisors.size() - 1));
        }
    }


    // Funkcja do ewaluacji równań
    private static int evaluateEquation(String expression) {
        Stack<Integer> numbers = new Stack<>();
        Stack<Character> operations = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // Jeśli liczba, dodajemy do stosu liczb
            if (Character.isDigit(c)) {
                int num = 0;
                while (Character.isDigit(c)) {
                    num = num * 10 + (c - '0');
                    i++;
                    if (i < expression.length()) {
                        c = expression.charAt(i);
                    } else {
                        break;
                    }
                }
                i--;
                numbers.push(num);
            }
            // Jeśli operator, przetwarzamy poprzednie operacje zgodnie z priorytetem
            else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operations.isEmpty() && hasPrecedence(c, operations.peek())) {
                    numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.push(c);
            }
        }

        // Przetwarzamy pozostałe operacje
        while (!operations.isEmpty()) {
            numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    public static boolean hasPrecedence(char op1, char op2) {
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }

    public static int applyOperation(char operation, int b, int a) {
        switch (operation) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new UnsupportedOperationException();
                }
                return a / b;
        }
        return 0;
    }

    private static char getRandomOperator(Random random, char... operators) {
        return operators[random.nextInt(operators.length)];
    }

    // Metoda do aktualizacji przycisków poziomów po przejściu do kolejnego
    private void updateLevelButtons() {
        Component[] components = levelsPanel.getComponents();

        for (int i = 0; i < components.length; i++) {
            JButton levelButton = (JButton) components[i];
            int level = Integer.parseInt(levelButton.getText().replaceAll("[^0-9]", ""));

            Color assignedColor = levelColors[i];

            if (level <= unlockedLevels) {
                levelButton.setEnabled(true);
                levelButton.setBackground(assignedColor);
                levelButton.setForeground(Color.BLACK);
                levelButton.setText("POZIOM " + level);
                levelButton.setBorder(BorderFactory.createLineBorder(assignedColor, 2));
                for (ActionListener al : levelButton.getActionListeners()) {
                    levelButton.removeActionListener(al);
                }
                int finalLevel = level;
                levelButton.addActionListener((ActionEvent e) -> uruchomPoziom(finalLevel));
            } else {
                levelButton.setEnabled(false); // Wyłącz przycisk
                Color desaturatedColor = desaturateColor(assignedColor, 0.3f);
                levelButton.setBackground(desaturatedColor);
                levelButton.setText("🔒 POZIOM " + level);
                levelButton.setBorder(BorderFactory.createLineBorder(desaturatedColor.darker(), 2, true));

                levelButton.setOpaque(true);
                levelButton.setContentAreaFilled(false);
                levelButton.setForeground(Color.DARK_GRAY);

                for (ActionListener al : levelButton.getActionListeners()) {
                    levelButton.removeActionListener(al);
                }
            }
        }
    }


    private void saveUserToFile() {
        if (!"GOŚĆ".equals(user.username)) {
            try {
                List<String> lines = new ArrayList<>();
                boolean userFound = false;

                // Wczytujemy wszystkie linie i sprawdzamy, czy użytkownik już istnieje
                try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith(user.username + ",")) {
                            userFound = true;
                            // Zastepujemy linię użytkownika nowymi danymi
                            lines.add(userToString());
                        } else {
                            lines.add(line);
                        }
                    }
                }

                if (!userFound) {
                    lines.add(userToString());
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd podczas zapisywania danych: " + e.getMessage(),
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String userToString() {
        StringBuilder data = new StringBuilder();
        data.append(user.username).append(",").append(user.unlockedLevels);
        for (int time : user.bestTimes) {
            data.append(",").append(time);
        }
        for (int star : stars) {
            data.append(",").append(star);
        }
        return data.toString();
    }

    public static Set<String> loadUniqueUsernames(String filePath) {
        Set<String> usernames = new TreeSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    usernames.add(parts[0]);
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas ładowania nazw użytkowników: " + e.getMessage());
        }
        return usernames;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MathRacingGame::new);
    }
}