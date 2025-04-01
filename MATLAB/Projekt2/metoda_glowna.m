function metoda_glowna(wyniki, bledy)
    % Liczba przypadków
    num_cases = size(wyniki, 1);

    % Określenie układu subplotów: 2 wiersze i wystarczająca liczba kolumn
    num_rows = 2;
    num_columns = ceil(num_cases / num_rows); % Zaokrąglanie w górę do pełnej liczby kolumn

    % Tworzenie nowej figury
    figure;

    % Iteracja przez przypadki testowe
    for i = 1:num_cases
        % Przygotowanie danych do wykresu
        blad_AX_crout = bledy{i, 2}; % Błąd AX dla metody Crout
        blad_XA_crout = bledy{i, 4}; % Błąd XA dla metody Crout
        dane_do_wykresu = [blad_AX_crout, blad_XA_crout]; % Dane jako wektor

        % Ustawienie domyślnego zakresu dla osi Y, jeśli dane są nieprawidłowe
        max_wartosc = max(dane_do_wykresu);
        if isnan(max_wartosc) || max_wartosc <= 0
            max_wartosc = 1; % Domyślna maksymalna wartość osi Y
        end

        % Tworzenie subplotu dla każdego przypadku
        subplot(num_rows, num_columns, i); % Układ: 2 wiersze, num_columns kolumn, pozycja i
        bar(dane_do_wykresu); % Wykres słupkowy

        % Ustawienia wykresu
        title(['Przypadek ', num2str(i)]);
        xlabel('Typ błędu');
        ylabel('Wartość błędu');
        xticks(1:2);
        xticklabels({'Błąd AX', 'Błąd XA'});
        ylim([0, max_wartosc * 1.2]); % Dynamiczna wysokość osi Y
        grid on;
    end

    % Dostosowanie wielkości okna figury
    set(gcf, 'Position', [100, 100, 800, 600]); % Skaluje okno wykresu
end
