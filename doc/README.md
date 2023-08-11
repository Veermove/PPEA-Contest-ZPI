## Przepływ konkrusu
1. Administrator tworzy nową edycję konkursu.
2. Administrator dodaje członków Jury do edycji konkursu.
3. Administrator aktywuje edycję konkursu.
4. Administrator dodaje aplikanta z organizacji (tworzy konto), który chce wziąć udział w konkursie.
    * Jeżeli organizacji nie ma w systemie zostaje utworzona.
    * Jeżeli organizacja była w systemie zostaje wybrana a dane uzupełnione.
    * Max 1 osoba z organizacji.
    * Aplikant odbiera maila / aktywuje konto
5. Aplikant rejstruje projekt.

## Użytkownicy
### Tworzenie konta
1. Admin odbiera maila.
2. Tworzy konto wg danych w mailu.
3. System wysyła maila z linkiem aktywacyjnym.

### Rodzaje użytkowników
1. Administrator systemu:
    * osoba z uprawnieniami do zarządzania systemem;
2. Członek Jury dla danej edycji konkursu:
    * osoba z uprawnieniami do oceny prac konkursowych;
    * rozróżniamy:
        * przewodniczącego Jury;
        * rozróżniamy zwykłych członków Jury;
3. Asesor:
    * może być asesorem **wiodącym** dla danego zgłoszenia projektu (0, 1 lub więcej zgłoszeń projektów).

4. Uczestnik/Aplikant:
    * osoba zarządzająca całym zgłoszeniem projektu;
5. Przedstawiciel Biura Nagrody dla danej edycji konkursu:
    * osoba z uprawnieniami do zarządzania edycjami konkursu;
    * może być ich wielu dla danej edycji konkursu;
6. Ekspert IPMA:
    * baza danych ekspertów IPMA;
    * może zostać zaproszony do bycia asesorem w danej edycji;
    * może być asesorem dla 0, 1, lub więcej proejtków w danej edycji.


## Funkcjonlaności
### Uruchamianie edycji
1. System umożliwia administratorowi utworzenie nowej edycji konkursu.
2. System umożliwia administratorowi modyfikację danych edycji konkursu.
3. System umożliwia administratorowi modyfikację składu Jury edycji konkursu (w tym przewodni-
czącego).
    * System dezaktywuje konto członka usuwanego ze składu Jury.
    * System aktywuje konto członka dodawanego do składu Jury aktualnej edycji.
4. System umożliwia administratorowi modyfikację składu Biura Nagrody edycji konkursu.
    * System dezaktywuje konto przedstawiciela usuwanego ze składu Biura Nagrody.
    * System aktywuje konto przedstawiciela dodawanego do składu Biura Nagrody aktualnej edycji.
5. System umożliwia administratorowi uruchomienie (aktywację) nowej edycji konkursu.
    * System kontroluje ograniczenie, zgodnie z którym aktualna może być maksymalnie jedna edycja konkursu – uniemożliwia aktywację drugiej edycji
    * System aktywuje konta członków Jury edycji konkursu.
6. System umożliwia administratorowi zakończenie aktualnej edycji konkursu.
    * System oznacza wszystkie dane ekspertów IPMA jako dane ‘do weryfikacji’.
    * System oznacza wszystkie dane organizacji jako dane ‘do weryfikacji’.
    * System dezaktywuje konta członków Jury oraz przedstawicieli Biura Nagrody edycji.
7. System umożliwia przedstawicielowi Biura Nagrody wprowadzenie i modyfikację danych aktualnej edycji konkursu:
    * daty zgłaszania aplikacji;
    * daty dostarczenia raportu aplikacyjnego;
    * planowanych dat opracowania:
        * oceny indywidualnej,
        * oceny wstępnej,
        * raportu z wizyty studyjnej,
        * oceny końcowej;
    * daty wizyty studyjnej
    * dokumentów/plików powiązanych z aktualną edycją:
        * regulaminu,
        * harmonogramu konkursu.
8. Proces przechodzi do [Zarządzanie aplikacjami po zakończeniu przyjmowania zgłoszeń](#zarządzanie-aplikacjami-po-zakończeniu-przyjmowania-zgłoszeń) po upływie ustalonego terminu.

### Zarządzanie aplikacjami po zakończeniu przyjmowania zgłoszeń
1. Przedstawiciel Biura Nagrody proponuje ekspertów (min: 1, max: 3) dla zgłosznia projektu.
    * Przedstawiciel BN w propozycji oznacza eksperta mającego zostać asesorem **wiodącym**.
    * System umożliwia przedstawicielowi BN usunięcie propozycji eksperta.
    * w przypadku propozycji dla 1 eksperta zostaje on asesorem **wiodącym**.
2. System wysyła maila do ekspertów z prośbą o zaakceptowanie propozycji:
    * system wyświetla status eksperta przy zgłoszeniu jako **OCZEKUJĄCY**, przed akceptacją.
    * system uznaje propozycje za odrzuconą po upływie [CZAS_WYGAŚNIĘCIA_PROPOZYCJI](#zmienne);
    * System umożliwia usunięcie propozycji asesora dla projektu (link w mailu wygasa).
3. System umożliwia ekspertowi zaakceptowanie lub odrzucenie propozycji.
    * Ekspert po akcpetacji staje się asesorem danego zgłoszenia projektu.
    * Po akceptacji eksperta zaczna się [opracowanie ocen indywidualnych asesorów dla danego zgłoszenia projektu](#opracowanie-ocen-indywidualnych-asesorów-dla-danego-zgłoszenia-projektu)
    * **Każde zgłoszenie musi mieć jednego asesora wiodącego**.

### Opracowanie ocen indywidualnych asesorów dla danego zgłoszenia projektu
1. Asesor przygotowuję ocenę indywidualną dla danego zgłoszenia projektu we wszystkich miarach PEM, która zostaje wyświetlana jako wersja robocza.
    * Ocena w wersji roboaczej może zostać zatwierdzona przez asesora.
    * Przy przejściu do nastepnego etapu konkursu (po upływie czasu na opracowanie ocen indywidualnych) system automatycznie zatwierdza ocenę w wersji roboczej.
2. Kiedy wszsystkie oceny indywidualne dla danego zgłoszenia projektu zostaną zatwierdzone, rozpoczyna się [opracowanie oceny wstępnej dla danego zgłoszenia projektu](#opracowanie-oceny-wstępnej-dla-danego-zgłoszenia-projektu).

### Opracowanie oceny wstępnej dla danego zgłoszenia projektu
1. Asesor wiodący wprowadza ocenę wstpęną dla danego zgłoszenia projektu we wszystkich miarach PEM.
    * Ocena w wersji roboaczej może zostać zatwierdzona przez asesora wiodącego.
    * Przy przejściu do nastepnego etapu konkursu (po upływie czasu na opracowanie oceny wstępnej) system automatycznie zatwierdza ocenę w wersji roboczej.




## Definicje
Organizacja - podmiot zgłaszający projekt.
PEM - Project Excellence Model

## ZMIENNE
CZAS_WYGAŚNIĘCIA_PROPOZYCJI - czas po którym propozycja eksperta jest odrzucana = 14 dni
