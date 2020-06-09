package ion.hyperon.groupproject;

import org.junit.Test;

import static org.junit.Assert.*;

public class GraphicCardTester {

    @Test
    public void testConstructor() {

        GraphicCard g = new GraphicCard();

        assertEquals("name is correct", "noName", g.name);
        assertEquals("Manufacturer is correct", "Unknown", g.manufacturer);

        assertEquals("Price is correct", 0.f, g.price, 0.0);
        assertEquals("Ram is correct", 0.f, g.ram, 0.0);
        assertEquals("PCI is correct", 0.f, g.PCI, 0.0);

        assertEquals("Fans is correct", 0, g.fans);
        assertEquals("hdmi is correct", 0, g.hdmi);
        assertEquals("DisplayPorts is correct", 0, g.displayPorts);
        assertEquals("PCI Lanes is correct", 0, g.PCI_Lane);

        assertEquals("Heaven Score is correct", 0, g.heavenScore, 0.0);
        assertEquals("Heaven Average fps is correct", 0.f, g.heavenAvgFps, 0.0);
        assertEquals("Heaven Max fps is correct", 0.f, g.heavenMaxFps, 0.0);
        assertEquals("Heaven Min fps is correct", 0.f, g.heavenMinFps, 0.0);

        assertEquals("Valley Score is correct", 0, g.valleyScore, 0.0);
        assertEquals("Valley Average fps is correct", 0.f, g.valleyAvgFps, 0.0);
        assertEquals("Valley Max fps is correct", 0.f, g.valleyMaxFps, 0.0);
        assertEquals("Valley Min fps is correct", 0.f, g.valleyMinFps, 0.0);

        assertEquals("Superposition Score is correct", 0, g.superpositionScore, 0.0);
        assertEquals("Superposition Average fps is correct", 0.f, g.superpositionAvgFps, 0.0);
        assertEquals("Superposition Max fps is correct", 0.f, g.superpositionMaxFps, 0.0);
        assertEquals("Superposition Min fps is correct", 0.f, g.superpositionMinFps, 0.0);

        assertEquals("Skydiver Score is correct", 0, g.skydiverScore, 0.0);
        assertEquals("Night Raid score is correct", 0, g.nightRaidScore, 0.0);
    }
}
