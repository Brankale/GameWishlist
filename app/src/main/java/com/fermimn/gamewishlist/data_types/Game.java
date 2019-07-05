package com.fermimn.gamewishlist.data_types;

import java.util.List;

// TODO: add documentation

public class Game extends GamePreview {

    private List<String> m_genres;
    private String m_description;
    private String m_officialSite;
    private String m_players;
    private boolean m_validForPromotions;
    private List<Promo> m_promos;

    public Game() {
    }

    public List<String> getGenres() {
        return m_genres;
    }

    public void setGenres(List<String> genres) {
        m_genres = genres;
    }

    public String getDescription() {
        return m_description;
    }

    public void setDescription(String description) {
        m_description = description;
    }

    public String getOfficialSite() {
        return m_officialSite;
    }

    public void setOfficialSite(String officialSite) {
        m_officialSite = officialSite;
    }

    public String getPlayers() {
        return m_players;
    }

    public void setPlayers(String players) {
        m_players = players;
    }

    public boolean isValidForPromotions() {
        return m_validForPromotions;
    }

    public void setValidForPromotions(boolean validForPromotions) {
        m_validForPromotions = validForPromotions;
    }

    public List<Promo> getPromo() {
        return m_promos;
    }

    public void setPromo(List<Promo> promo) {
        m_promos = promo;
    }

}
