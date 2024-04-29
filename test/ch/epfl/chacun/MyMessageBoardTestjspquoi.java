package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MyMessageBoardTestjspquoi {
    @Test
    void testPoints() {
        // Création d'un tableau d'affichage vide
        MessageBoard emptyMessageBoard = new MessageBoard(new TextMakerImpl(), new ArrayList<>());

        // Vérification que le tableau d'affichage vide retourne une map vide
        assertTrue(emptyMessageBoard.points().isEmpty());

        // Création de messages avec des points pour différents joueurs
        TextMakerImpl textMaker = new TextMakerImpl();
        MessageBoard.Message message1 = new MessageBoard.Message(textMaker.playerClosedForestWithMenhir(PlayerColor.RED), 10, Set.of(PlayerColor.RED, PlayerColor.BLUE), new HashSet<>());
        MessageBoard.Message message2 = new MessageBoard.Message(textMaker.playerClosedForestWithMenhir(PlayerColor.BLUE), 15, Set.of(PlayerColor.BLUE), new HashSet<>());
        MessageBoard.Message message3 = new MessageBoard.Message(textMaker.playerClosedForestWithMenhir(PlayerColor.RED), 5, Set.of(PlayerColor.RED), new HashSet<>());

        // Création d'un tableau d'affichage
        List<MessageBoard.Message> messages = new ArrayList<>();
        messages.add(message1);
        messages.add(message2);
        messages.add(message3);
        MessageBoard messageBoard = new MessageBoard(textMaker, messages);

        // Création de la map attendue
        Map<PlayerColor, Integer> expectedPoints = new HashMap<>();
        expectedPoints.put(PlayerColor.RED, 15);
        expectedPoints.put(PlayerColor.BLUE, 25);

        // Vérification que la méthode points() retourne la map attendue
        assertEquals(expectedPoints, messageBoard.points());
    }

    @Test
    void testWithScoredForest() {
        // Création de messages avec des points pour différents joueurs
        TextMakerImpl textMaker = new TextMakerImpl();
        MessageBoard.Message message1 = new MessageBoard.Message(textMaker.playerClosedForestWithMenhir(PlayerColor.RED), 10, Set.of(PlayerColor.RED, PlayerColor.BLUE), new HashSet<>());


        List<MessageBoard.Message> messages = new ArrayList<>();
        messages.add(message1);
        MessageBoard messageBoard = new MessageBoard(textMaker, messages);
        Zone.Forest forest = new Zone.Forest(561, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest2 = new Zone.Forest(170, Zone.Forest.Kind.WITH_MUSHROOMS);
        Set<Zone.Forest> forests = new HashSet<>();
        forests.add(forest);
        forests.add(forest2);
        Area<Zone.Forest> forestAreaWithOutOccupant = new Area<>(forests, List.of(), 2);

        // Forest with no occupant
        assertEquals(messageBoard, messageBoard.withScoredForest(forestAreaWithOutOccupant));

        PlayerColor playerColor1 = PlayerColor.RED;
        PlayerColor playerColor2 = PlayerColor.BLUE;
        List<PlayerColor> playerColors = new ArrayList<>();
        playerColors.add(playerColor1);
        playerColors.add(playerColor2);

        Set<PlayerColor> players = new HashSet<>();
        players.add(playerColor1);
        players.add(playerColor2);

        Area<Zone.Forest> forestArea = new Area<>(forests, playerColors, 2);
        assertEquals(textMaker.playersScoredForest(players, Points.forClosedForest(2, 2), 2, 2), messageBoard.withScoredForest(forestArea).messages().getLast().text());

    }

    @Test
    void testWithClosedForestWithMenhir() {
        // Création de messages avec des points pour différents joueurs
        TextMakerImpl textMaker = new TextMakerImpl();
        MessageBoard.Message message1 = new MessageBoard.Message(textMaker.playerClosedForestWithMenhir(PlayerColor.RED), 10, Set.of(PlayerColor.RED, PlayerColor.BLUE), new HashSet<>());


        List<MessageBoard.Message> messages = new ArrayList<>();
        messages.add(message1);
        MessageBoard messageBoard = new MessageBoard(textMaker, messages);
        Zone.Forest forest = new Zone.Forest(561, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest2 = new Zone.Forest(170, Zone.Forest.Kind.WITH_MUSHROOMS);
        Set<Zone.Forest> forests = new HashSet<>();
        forests.add(forest);
        forests.add(forest2);
        Area<Zone.Forest> forestAreaWithOutOccupant = new Area<>(forests, List.of(), 2);

        // Forest with no menhir
        assertEquals(messageBoard, messageBoard.withScoredForest(forestAreaWithOutOccupant));

        PlayerColor playerColor1 = PlayerColor.RED;
        PlayerColor playerColor2 = PlayerColor.BLUE;
        List<PlayerColor> playerColors = new ArrayList<>();
        playerColors.add(playerColor1);
        playerColors.add(playerColor2);

        Set<PlayerColor> players = new HashSet<>();
        players.add(playerColor1);
        players.add(playerColor2);

        Zone.Forest forest3 = new Zone.Forest(170, Zone.Forest.Kind.WITH_MENHIR);
        forests.add(forest3);
        Area<Zone.Forest> forestArea = new Area<>(forests, playerColors, 2);
        assertEquals(textMaker.playerClosedForestWithMenhir(PlayerColor.RED), messageBoard.withClosedForestWithMenhir(PlayerColor.RED, forestArea).messages().getLast().text());
    }

    @Test
    void testWithScoredRiver() {
        // Création de messages avec des points pour différents joueurs
        TextMakerImpl textMaker = new TextMakerImpl();
        MessageBoard.Message message1 = new MessageBoard.Message(textMaker.playerClosedForestWithMenhir(PlayerColor.RED), 10, Set.of(PlayerColor.RED, PlayerColor.BLUE), new HashSet<>());


        List<MessageBoard.Message> messages = new ArrayList<>();
        messages.add(message1);
        MessageBoard messageBoard = new MessageBoard(textMaker, messages);

        Zone.Lake lake1 = new Zone.Lake(1, 2, null);
        Area<Zone.River> riverWithOutOccupant = new Area<>(
                Set.of(
                        new Zone.River(1, 3, lake1),
                        new Zone.River(2, 2, null)
                ),
                List.of(),
                0
        );

        // River with no occupant
        assertEquals(messageBoard, messageBoard.withScoredRiver(riverWithOutOccupant));

        PlayerColor playerColor1 = PlayerColor.RED;
        PlayerColor playerColor2 = PlayerColor.BLUE;
        Area<Zone.River> river = new Area<>(
                Set.of(
                        new Zone.River(562, 3, lake1),
                        new Zone.River(171, 2, null)
                ),
                List.of(playerColor1, playerColor2),
                2
        );


        //assertEquals(textMaker.playersScoredRiver(Set.of(playerColor1, playerColor2), Points.forClosedRiver(2, 7), 7, 2), messageBoard.withScoredRiver(river).messages().getLast().text());
    }

    @Test
    void testWithScoredLogboat() {
        // Création de messages avec des points pour différents joueurs
        TextMakerImpl textMaker = new TextMakerImpl();
        MessageBoard.Message message1 = new MessageBoard.Message(textMaker.playerClosedForestWithMenhir(PlayerColor.RED), 10, Set.of(PlayerColor.RED, PlayerColor.BLUE), new HashSet<>());


        List<MessageBoard.Message> messages = new ArrayList<>();
        messages.add(message1);
        MessageBoard messageBoard = new MessageBoard(textMaker, messages);

        Zone.Lake lake1 = new Zone.Lake(1, 2, Zone.SpecialPower.LOGBOAT);
        Area<Zone.Water> riverWithOutLake = new Area<>(
                Set.of(
                        new Zone.River(1, 3, null),
                        new Zone.River(2, 2, null)
                ),
                List.of(),
                0
        );

        // River system with no lake
        assertThrows(IllegalArgumentException.class, () -> messageBoard.withScoredLogboat(PlayerColor.BLUE, riverWithOutLake));

        PlayerColor playerColor1 = PlayerColor.RED;
        PlayerColor playerColor2 = PlayerColor.BLUE;
        Area<Zone.Water> riverSystem = new Area<>(
                Set.of(
                        new Zone.River(562, 3, lake1),
                        new Zone.River(171, 2, null),
                        lake1),
                List.of(playerColor1, playerColor2),
                2
        );


        assertEquals(textMaker.playerScoredLogboat(PlayerColor.BLUE, Points.forLogboat(1), 1), messageBoard.withScoredLogboat(PlayerColor.BLUE, riverSystem).messages().getLast().text());
    }

    @Test
    void testWithScoredRiverSystem() {
        // Création de messages avec des points pour différents joueurs
        TextMakerImpl textMaker = new TextMakerImpl();
        MessageBoard.Message message1 = new MessageBoard.Message(textMaker.playerClosedForestWithMenhir(PlayerColor.RED), 10, Set.of(PlayerColor.RED, PlayerColor.BLUE), new HashSet<>());


        List<MessageBoard.Message> messages = new ArrayList<>();
        messages.add(message1);
        MessageBoard messageBoard = new MessageBoard(textMaker, messages);

        Zone.Lake lake1 = new Zone.Lake(1, 2, Zone.SpecialPower.LOGBOAT);
        // River system with no occupant
        Area<Zone.Water> riverWithOutOccupant = new Area<>(
                Set.of(
                        new Zone.River(1, 2, null),
                        new Zone.River(2, 3, null)
                ),
                List.of(),
                0
        );
        assertEquals(messageBoard, messageBoard.withScoredRiverSystem(riverWithOutOccupant));

        // River system with no fish
        Area<Zone.Water> riverWithOutFish = new Area<>(
                Set.of(
                        new Zone.River(1, 0, null),
                        new Zone.River(2, 0, null)
                ),
                List.of(PlayerColor.RED),
                0
        );
        assertEquals(messageBoard, messageBoard.withScoredRiverSystem(riverWithOutOccupant));

        PlayerColor playerColor1 = PlayerColor.RED;
        PlayerColor playerColor2 = PlayerColor.BLUE;
        Area<Zone.Water> riverSystem = new Area<>(
                Set.of(
                        new Zone.River(562, 3, lake1),
                        new Zone.River(171, 2, null),
                        lake1),
                List.of(playerColor1, playerColor2),
                2
        );


        assertEquals(textMaker.playerScoredLogboat(PlayerColor.BLUE, Points.forLogboat(1), 1), messageBoard.withScoredLogboat(PlayerColor.BLUE, riverSystem).messages().getLast().text());
    }
}