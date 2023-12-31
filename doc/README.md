## Przepływ konkrusu
1. Administrator tworzy nową edycję konkursu.
2. Administrator dodaje członków Jury do edycji konkursu.
3. Administrator aktywuje edycję konkursu.
4. Administrator dodaje aplikanta z organizacji (tworzy konto), który chce wziąć udział w konkursie.
    * Jeżeli organizacji nie ma w systemie, zostaje ona utworzona.
    * Jeżeli organizacja była w systemie, zostaje wybrana a dane uzupełnione.
    * Max 1 osoba z organizacji.
    * Aplikant odbiera maila / aktywuje konto
5. Aplikant rejstruje projekt.
6. Aplikant dokonuje zmian w projekcie, do momentu upłynięcia terminu - planowanej daty dostarczenia raportu aplikacyjnego.
7. Biuro nagrody wysyła propozycje zostania asesorami do ekspertów PEM, dla każdego zgłoszenia projektu.
   * Jeżeli dany ekspert PEM był już asesorem w poprzednich edycjach, jego istniejące konto zostaje przypisane do zgłoszeń projektu w nowej edycji.
   * Jeżeli dany ekspert PEM nie był nigdy asesorem, zostaje utworzone dla niego nowe konto z rolą asesora.
8. Eksperci PEM podejmują decyzję, co do zostania asesorami zgłoszenia projektu.
9. Biuro nagrody zapewnia co najmniej jednego asesora wiodącego dla każdego zgłoszenia projektu.
10. Asesorzy opracowują oceny indywidualne dla danego zgłoszenia projektu.
11. Asesor wiodący, w porozumieniu z zespołem asesorów opracowuje ocenę wstępną dla danego zgłoszenia projektu.
12. Jury podejmuje decyzję, czy projekt powinien zostać skierowany do wizyty studyjnej.
    * Jeżeli decyzja jest pozytywna, Jury dodaje pytania do zgłoszenia projektu
    * Jeżeli decyzja jest negatywna, proces przetwarzania danego zgłoszenia projektu zostaje zakończony
13. Zespół asesorów odpowiada na pytania zadane przez Jury.
14. Po dodaniu odpowiedzi na wszystkie pytania, asesor wiodący dla danego zgłoszenia zatwierdza raport z wizyty studyjnej.
15. Asesor wiodący opracowuje ocenę końcową dla danego zgłoszenia.

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
        * zwykłych członków Jury;
3. Asesor:
    * może być przypisany do oceny co najwyżej jednego zgłoszenia projektu w danej edycji
    * może pełnić funkcję asesora wiodącego

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
1. Asesor przygotowuję ocenę indywidualną dla danego zgłoszenia projektu we wszystkich kryteriach PEM, która zostaje wyświetlana jako wersja robocza.
    * Ocena w wersji roboaczej może zostać zatwierdzona przez asesora.
    * Przy przejściu do nastepnego etapu konkursu (po upływie czasu na opracowanie ocen indywidualnych) system automatycznie zatwierdza ocenę w wersji roboczej.
    * Nie można przejść do kolejnego etapu, jeżeli wszyscy asesorzy nie wprowadzili jeszcze ocen indywidualnych (w wersji zatwierdzonej lub roboczej)
    * Jeżeli co najmniej jeden Asesor nie wprowadził ocen indywidualnych, w momencie przejścia do następnego etapu konkursu wysyłana jest wiadomość e-mail do Asesora/Asesorów, którzy nie dodali ocen indywidualnych. Kolejna próba przejścia do kolejnego etapu zostaje podjęta po [INTERWAŁ_SPRAWDZANIA_OCEN](#zmienne)
2. Kiedy wszsystkie oceny indywidualne dla danego zgłoszenia projektu zostaną zatwierdzone, rozpoczyna się [opracowanie oceny wstępnej dla danego zgłoszenia projektu](#opracowanie-oceny-wstępnej-dla-danego-zgłoszenia-projektu).

### Opracowanie oceny wstępnej dla danego zgłoszenia projektu
1. Asesor wiodący wprowadza ocenę wstępną dla danego zgłoszenia projektu we wszystkich kryteriach PEM.
    * Ocena w wersji roboczej może zostać zatwierdzona przez asesora wiodącego.
    * Przy przejściu do nastepnego etapu konkursu (po upływie czasu na opracowanie oceny wstępnej) system automatycznie zatwierdza ocenę w wersji roboczej.
    * Nie można przejść do kolejnego etapu, jeżeli wszyscy asesorzy wiodący nie wprowadzili jeszcze ocen wstępnych (w wersji zatwierdzonej lub roboczej)
    * Jeżeli co najmniej jeden Asesor wiodący nie wprowadził ocen, w momencie przejścia do następnego etapu konkursu wysyłana jest wiadomość e-mail do Asesora/Asesorów, którzy nie dodali ocen wstępnych. Kolejna próba przejścia do kolejnego etapu zostaje podjęta po [INTERWAŁ_SPRAWDZANIA_OCEN](#zmienne)
2. Kiedy ocena wstępna dla danego zgłoszenia projektu zostanie zatwierdzona, Jury otrzymuje informację o ocenie wstępnej dla danego zgłoszenia projektu. Jury może [zakwalifikować projekt do wizyty studyjnej](#kwalifikacja-do-wizyt-studyjnych-i-wizyty-studyjne). 

### Kwalifikacja do wizyt studyjnych i wizyty studyjne
1. Po otrzymaniu oceny wstępnej dla projektu, system umożliwia przewodniczącemu Jury podjęcie decyzji o odrzuceniu zgłoszenia projektu lub skierowaniu go do wizyty studyjnej.
2. System wysyła aplikantowi informację o podjętej decyzji poprzez wiadomość email.
3. W przypadku podjęcia decyzji o skierowaniu zgłoszenia projektu do wizyty studyjnej, system umożliwia przewodniczącemu Jury dodanie pytań do wizyty studyjnej. W przeciwnym przypadku, proces przetwarzania danego zgłoszenia projektu zostaje zakończony.
4. Po dodaniu pytań przez przewodniczącego Jury, system umożliwia zespołowi asesorów dodanie odpowiedzi na zadane pytania.
5. Kiedy odpowiedzi na wszystkie pytania zostaną dodane, asesor wiodący ma możliwość zatwierdzenia raportu z wizyty studyjnej.
6. Po zatwierdzeniu przez asesora wiodącego raportu z wizyty studyjnej, rozpoczyna się [opracowanie oceny końcowej dla danego zgłoszenia projektu](#opracowanie-oceny-końcowej-dla-danego-zgłoszenia-projektu)

### Opracowanie oceny końcowej dla danego zgłoszenia projektu
1. Asesor wiodący wprowadza ocenę końcową dla danego zgłoszenia projektu we wszystkich kryteriach PEM.
    * Ocena w wersji roboczej może zostać zatwierdzona przez asesora wiodącego.
2. Po zatwierdzeniu oceny, system przesyła ocenę do aplikanta poprzez wiadomość email.


## Definicje
Organizacja - podmiot zgłaszający projekt.
PEM - Project Excellence Model

## ZMIENNE
CZAS_WYGAŚNIĘCIA_PROPOZYCJI - czas po którym propozycja eksperta jest odrzucana = 14 dni
INTERWAŁ_SPRAWDZANIA_OCEN - czas po którym zostanie podjęta kolejna próba przejścia do kolejnego etapu po wykryciu brakujących ocen = 1 godzina