/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nico;

import java.util.List;

/**
 *
 * @author pancho
 */
public class FootballPlayer {
    private int dorsal;
    private String name;
    private List demarcation;
    private String team;

    public FootballPlayer(int dorsal, String name, List demarcation,
            String team) {
        this.dorsal = dorsal;
        this.name = name;
        this.demarcation = demarcation;
        this.team = team;
    }

    // getter y setter

    @Override
    public String toString() {
        return "Dorsal=" + dorsal + ", Name=" + name + ", Demarcation="
                + demarcation + ", Team=" + team;
    }
}
