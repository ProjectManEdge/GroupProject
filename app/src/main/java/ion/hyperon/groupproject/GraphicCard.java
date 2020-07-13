package ion.hyperon.groupproject;

public class GraphicCard {

    public String name;
    public String manufacturer;

    public float price;
    public float ram_size;
    public String ram_type;
    public float PCI;

    public int fans;
    public int hdmi;
    public int displayPorts;
    public int PCI_Lane;

    public double heavenScore;
    public float heavenAvgFps;
    public float heavenMaxFps;
    public float heavenMinFps;

    public double valleyScore;
    public float valleyAvgFps;
    public float valleyMaxFps;
    public float valleyMinFps;

    public double superpositionScore;
    public float superpositionAvgFps;
    public float superpositionMaxFps;
    public float superpositionMinFps;

    public double skydiverScore;
    public double nightRaidScore;

    public GraphicCard() {
        name = "noName";
        manufacturer = "Unknown";

        price = 0.00f;
        ram_size = 0.f;
        ram_type = "";
        PCI = 0.f;

        fans = 0;
        hdmi = 0;
        displayPorts = 0;
        PCI_Lane = 0;

        heavenScore = 0.;
        heavenAvgFps = 0.f;
        heavenMaxFps = 0.f;
        heavenMinFps = 0.f;

        valleyScore = 0.;
        valleyAvgFps = 0.f;
        valleyMaxFps = 0.f;
        valleyMinFps = 0.f;

        superpositionScore = 0.;
        superpositionAvgFps = 0.f;
        superpositionMaxFps = 0.f;
        superpositionMinFps = 0.f;

        skydiverScore = 0.;
        nightRaidScore = 0.;


    }
}


