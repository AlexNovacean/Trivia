package ro.alex.trivia.model;

public enum TriviaCategory {
    SCIENCE("This category includes questions that cover fundamental scientific concepts, principles, and discoveries across disciplines like chemistry, physics, biology, and astronomy. Topics explore the periodic table, planetary science, forces and motion, cellular biology, and notable scientific theories and phenomena."),
    ENTERTAINMENT("Encompassing the realms of movies, television, music, and pop culture, this category tests knowledge about film protagonists, TV characters, musical hits, and iconic symbols of popular media. It covers details from animated features to famous bands, highlighting key moments and figures from entertainment history."),
    HISTORY("This category focuses on significant historical events, influential leaders, ancient civilizations, and landmark discoveries. It includes inquiries about political leaders, groundbreaking treaties, wars, and revolutions, as well as cultural and scientific contributions that have shaped human history."),
    GEOGRAPHY("Geography questions challenge understanding of the Earth's landscapes, continents, countries, and natural landmarks. Topics may include locations of major cities, key geographical features like rivers and mountains, and distinctions between various regions and climates around the world."),
    ARTS("Spanning visual arts, literature, architecture, and music, this category delves into the works and creators that have left a lasting impact. It covers renowned paintings, famous playwrights, classical compositions, and architectural marvels, highlighting their styles and historical significance within the art world.");

    private final String description;

    TriviaCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
