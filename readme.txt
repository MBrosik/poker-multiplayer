1. Omówienie zasad gry.

Wzorowałem się na grze Poker Texas Holdem
https://pokertexas.net/zasady-gry-w-texas-holdem/

Na początku gracz musi stworzyć pokój, albo do niego dołączyć.
Gra się rozpocznie jeżeli wszyscy gracze zaznaczą, że już są gotowi.

Każdy gracz ma na początku 200 źetonów.
Również jest na początku wybierany Small Blind i Big Blind
Pierwszy daje po 5 a drugi po 10.
Następni gracze muszą wyrównać, albo dać kwotę wyższą.
Jeżeli gracze będą ciągle podawać tą samą kwotę (tzn. Check) to system zacznie nową "rundę obstawiania"
i poda kolejne karty na stół.
Tak zwany Show Down rozpocznie się jeżeli pozostanie 1 gracz (pozostali spasowali),
lub gdy będzie 5 kart na stole.
Po tak skończonej turze gracze mogą zadecydować, czy chcą nadal grę toczyć, czy już nie.

2. Sposób uruchamiania gry.

Grę można odpalić na dwa sposoby:
    a)
        - Odpalić serwer za pomocą Intellij Idea
        - Odpalić klienta za pomocą rozszerzenia Multi Run
            https://plugins.jetbrains.com/plugin/7248-multirun
    b)
        - Zbudować projekt (odpalić na głównym pom.xml komendę mvn package)
        - W folderach target (w poker-client i w poker-server pojawią się pliki "*-jar-with-dependencies.jar"
        - Najpierw odpalamy serwer za pomocą komendy java -jar "poker-server-1.0-SNAPSHOT-jar-with-dependencies.jar"
        - A później klientów za pomocą java -jar "poker-client-1.0-SNAPSHOT-jar-with-dependencies.jar"

3. Omówienie protokołu komunikacyjnego.

Do tworzenia stringów użyłem biblioteki GSON, która przerabia klasę tak, żeby była w formacie JSON.
Obiekt (klasa poker.commons.socket.ReceiveData) jest w formacie:

{
    "action":"Informacja czego będzie dotyczył komunikat",
    "data": <Obiekt>
}

action jest to enum poker.commons.socket.data_types.ActionType
data jest ustawiona na typ Object. Tutaj mogą zostać użyte różne klasy (albo boolean, int itd.),
które znajdują się w pakiecie poker.commons.socket.data_types

Możliwe komunikaty:

Tworzenie pokoju:
klient:
{"action":"CREATE_ROOM"} - tworzenie pokoju
{"action":"JOIN_ROOM","data":1704199032783011} - dołączenie do pokoju, gdzie data to klucz do pokoju
{"action":"READY_TO_PLAY"} - oznaczenie, że gracz jest gotowy do gry

server:
{"action":"CREATE_ROOM", "data":"123"} - informacja o stworzeniu pokoju. Data to kod do pokoju
{"action":"JOIN_ROOM", "data":"ADDED"} - informacja o dodaniu pokoju. Możliwe wartości data: "ADDED", "ROOM_IS_FULL", "NOT_EXISTS"

Rozrywka:

klient:
{"action":"BET","data":20} - informacja o postawionej stawce danego gracza
{"action":"PASS"} - informacje o tym, że gracz pasuje

server:
{"action":"BET", "data": {...}} - informacje o tym kto teraz betuje, jaka jest teraz najwyższa stawka na kole, czy gracz może nadal grać

Pod "data" się kryje:
public class BetInfo {
    private int money;
    private int currentBet;
    private boolean myBet;
    private boolean passed;
    private boolean moneyAvailable;
}

{"action":"NEXT_ROUND", "data": {"cardsOnTheTable": [{"rank": "ONE", "suit": "SPADES"}]}} - informacja o "następnej rundzie betowania" w dacie są informacje o kartach na stole.


{"action":"END_TURN", data: {...}} - informacja o końcu rundy. Kto wygrał, jakie karty kto miał, jaki układ itd.

Pod "data" się kryje:
public class EndGameInfo {
    private List<Card> cardsOnTable;
    private List<Card> cardsInHand;
    private boolean win;
    private int countOfWinner;
    private String variation;
    private int howMuchIWon;
    private int myMoney;
}
