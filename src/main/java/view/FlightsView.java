package view;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import model.Airport;
import model.Flight;

import java.util.Collection;

public class FlightsView extends BorderPane {

    /** The graph model */
    private final Graph<Airport, Flight> graph;

    /** Graph visualization panel */
    private final SmartGraphPanel<Airport, Flight> graphView;

    /** displays total number of airports */
    private Label labelNumberAirports;

    /** displays total number of flights */
    private Label labelNumberFlights;

    /** displays the airport with the most inbound/outbound flights */
    private Label labelBusiestAirport;

    /** displays the number of inbound/outbound flights of the busiest airport */
    private Label labelBusiestAirportNumberFlights;

    /** */

    private ObservableList<Vertex<Airport>> listAirportsToRemove;
    private ObservableList<Vertex<Airport>> listAirportsFrom;
    private ObservableList<Vertex<Airport>> listAirportsTo;
    private ObservableList<Edge<Flight, Airport>> listFlights;

    public FlightsView() {
        this.graph = new GraphEdgeList<>();
        createInitialModel();

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        this.graphView = new SmartGraphPanel<>(graph, strategy);

        doLayout();
    }

    private void createInitialModel() {
        // TODO: create flight model - Figure 1 of assignment

        Vertex<Airport> sfo = graph.insertVertex(new Airport("SFO"));
        Vertex<Airport> ord = graph.insertVertex(new Airport("ORD"));
        Vertex<Airport> pvd = graph.insertVertex(new Airport("PVD"));
        Vertex<Airport> lga = graph.insertVertex(new Airport("LGA"));
        Vertex<Airport> hnl = graph.insertVertex(new Airport("HNL"));
        Vertex<Airport> lax = graph.insertVertex(new Airport("LAX"));
        Vertex<Airport> dfw = graph.insertVertex(new Airport("DFW"));
        Vertex<Airport> mia = graph.insertVertex(new Airport("MIA"));

        graph.insertEdge(hnl, lax, new Flight("UN3563", 2555));   // F1
        graph.insertEdge(hnl, lax, new Flight("DT1597", 2555));   // F2
        graph.insertEdge(lax, sfo, new Flight("UN9375", 337));   // F3
        graph.insertEdge(lax, sfo, new Flight("AM4526", 337));   // F4
        graph.insertEdge(lax, ord, new Flight("UN4836", 1743));  // F5
        graph.insertEdge(lax, ord, new Flight("VA2001", 1743));  // F6
        graph.insertEdge(sfo, ord, new Flight("UN1475", 1843));  // F7
        graph.insertEdge(sfo, ord, new Flight("AL7854", 1843));  // F8
        graph.insertEdge(lax, dfw, new Flight("SP1020", 1233));  // F9
        graph.insertEdge(lax, dfw, new Flight("AM4582", 1233));  // F10
        graph.insertEdge(ord, dfw, new Flight("UN4568", 802));   // F11
        graph.insertEdge(dfw, lga, new Flight("SP4512", 1387));  // F12
        graph.insertEdge(ord, pvd, new Flight("AM4520", 849));   // F13
        graph.insertEdge(ord, pvd, new Flight("UN7812", 849));   // F14
        graph.insertEdge(pvd, mia, new Flight("FT1000", 1205));  // F15
        graph.insertEdge(lga, mia, new Flight("FT4021", 1099));  // F16
        graph.insertEdge(lga, mia, new Flight("AM1026", 1099));  // F17
        graph.insertEdge(dfw, mia, new Flight("AM5267", 1120));  // F18
    }

    private void addAirport(String airportCode) {
        // TODO: implement, check for errors and use showError(message) in case of error
        //  code must be valid, i.e., not empty

        if (airportCode.isBlank())
        {
            showError("Airport code must not be empty.");
            return;
        }

        try {
            graph.insertVertex(new Airport(airportCode));
        }
        catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void addFlight(Vertex<Airport> vertexFrom, Vertex<Airport> vertexTo, String code, String distance) {
        // TODO: implement, check for errors and use showError(message) in case of error
        //  Cannot add flights with the same airport as inbound/outbound

        try {
            double dist = Double.valueOf(distance);
            graph.insertEdge(vertexFrom, vertexTo, new Flight(code, dist));
        }
        catch (Exception e)
        {
            showError(e.getMessage());
        }
    }

    private void removeFlight(Edge<Flight, Airport> edge) {
        // TODO: implement, check for errors and use showError(message) in case of error

        try {
            graph.removeEdge(edge);
        }
        catch (Exception e)
        {
            showError(e.getMessage());
        }
    }

    private void removeAirport(Vertex<Airport> vertex) {
        // TODO: implement, check for errors and use showError(message) in case of error

        try {
            graph.removeVertex(vertex);
        }
        catch (Exception e)
        {
            showError(e.getMessage());
        }
    }

    private void updateStatistics() {
        // TODO: query model and update labels
        //  e.g., labelNumberAirports.setText( ?? );

        labelNumberAirports.setText( "" + graph.numVertices() );
        labelNumberFlights.setText( "" + graph.numEdges());

        Vertex<Airport> busiest = null;
        int busiestCount = -1;
        for (Vertex<Airport> airport : graph.vertices()) {
            Collection<Edge<Flight, Airport>> flights = graph.incidentEdges(airport);
            int count = flights.size();
            if (count > busiestCount) {
                busiestCount = count;
                busiest = airport;
            }
        }
        labelBusiestAirport.setText( busiest!=null? busiest.element().toString() : "<None>" );

        labelBusiestAirportNumberFlights.setText( busiest!=null? "" + busiestCount : "<NA>" );
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // STUDENTS -> THERE IS NOTHING TO DO BELOW THIS LINE

    public void takeOff() {
        this.graphView.init();
        updateStatistics();
        updateControls();
    }

    private void doLayout() {
        setStyle("-fx-background-color: #FFF;");

        // Top area - user interaction
        VBox top = new VBox(30);
        top.setPadding(new Insets(30));
        HBox firstRow = new HBox(10);
        HBox secondRow = new HBox(10);
        HBox thirdRow = new HBox(10);

        Button buttonAddAirport = new Button("Add");
        Button buttonRemoveAirport = new Button("Remove");
        Button buttonAddFlight = new Button("Add");
        Button buttonRemoveFlight = new Button("Remove");

        this.listAirportsToRemove = FXCollections.observableArrayList();
        this.listAirportsFrom = FXCollections.observableArrayList();
        this.listAirportsTo = FXCollections.observableArrayList();
        this.listFlights = FXCollections.observableArrayList();

        TextField textAirportCode = new TextField();
        textAirportCode.setPromptText("Airport code");
        TextField textFlightCode = new TextField();
        textFlightCode.setPromptText("Flight code");
        TextField textFlightDistance = new TextField();
        textFlightDistance.setPromptText("Flight distance");

        ComboBox<Vertex<Airport>> comboAirportsToRemove = new ComboBox<>(listAirportsToRemove);
        ComboBox<Vertex<Airport>> comboAirportsFrom = new ComboBox<>(listAirportsFrom);
        ComboBox<Vertex<Airport>> comboAirportsTo = new ComboBox<>(listAirportsTo);
        ComboBox<Edge<Flight, Airport>> comboFlightsToRemove = new ComboBox<>(listFlights);

        comboFlightsToRemove.setConverter(new EgdeFlightConverter());
        comboAirportsToRemove.setConverter(new VertexAirportConverter());
        comboAirportsFrom.setConverter(new VertexAirportConverter());
        comboAirportsTo.setConverter(new VertexAirportConverter());

        firstRow.getChildren().addAll(new Label("Add Airport: "), textAirportCode, buttonAddAirport,
                new Separator(Orientation.VERTICAL),
                new Label("Remove Airport: "), comboAirportsToRemove, buttonRemoveAirport);

        secondRow.getChildren().addAll(new Label("Add Flight From: "),
                comboAirportsFrom, new Label("To:"), comboAirportsTo, textFlightCode, textFlightDistance, buttonAddFlight);

        thirdRow.getChildren().addAll(new Label("Remove Flight: "), comboFlightsToRemove, buttonRemoveFlight);
        top.getChildren().addAll(firstRow, secondRow, thirdRow);

        setTop(top);

        // Center area - the graph visualization (airports and flights)
        setCenter(new SmartGraphDemoContainer(this.graphView));

        // Bottom area - statistics
        HBox bottom = new HBox(10);
        bottom.setPadding(new Insets(30));

        Label airportCount = new Label("Airport count: ");
        Label flightCount = new Label("Flight count: ");
        Label busiestAirport = new Label("Busiest Airport: ");
        Label busiestCount = new Label("Busiest Airport Flight Count: ");
        this.labelNumberAirports = new Label("##");
        this.labelNumberFlights = new Label("##");
        this.labelBusiestAirport = new Label("##");
        this.labelBusiestAirportNumberFlights = new Label("##");

        bottom.getChildren().addAll(airportCount, labelNumberAirports,
                flightCount, labelNumberFlights,
                busiestAirport, labelBusiestAirport,
                busiestCount, labelBusiestAirportNumberFlights);

        setBottom(bottom);

        // Bind events
        buttonAddAirport.setOnAction(event -> {
            String airportCode = textAirportCode.getText().trim();
            addAirport(airportCode);
            textAirportCode.clear();
            graphView.update();
            updateControls();
            updateStatistics();
        });

        buttonRemoveAirport.setOnAction(event -> {
            Vertex<Airport> vertex = comboAirportsToRemove.getSelectionModel().getSelectedItem();
            removeAirport(vertex);
            graphView.update();
            updateControls();
            updateStatistics();
        });

        buttonAddFlight.setOnAction(event -> {
            Vertex<Airport> vertexFrom = comboAirportsFrom.getSelectionModel().getSelectedItem();
            Vertex<Airport> vertexTo = comboAirportsTo.getSelectionModel().getSelectedItem();
            String flightCode = textFlightCode.getText();
            String flightDistance = textFlightDistance.getText();
            addFlight(vertexFrom, vertexTo, flightCode, flightDistance);
            textFlightCode.clear();
            textFlightDistance.clear();
            graphView.update();
            updateControls();
            updateStatistics();
        });

        buttonRemoveFlight.setOnAction(event -> {
            Edge<Flight, Airport> edge = comboFlightsToRemove.getSelectionModel().getSelectedItem();
            removeFlight(edge);
            graphView.update();
            updateControls();
            updateStatistics();
        });
    }

    private void updateControls() {
        Collection<Vertex<Airport>> airports = graph.vertices();
        Collection<Edge<Flight, Airport>> flights = graph.edges();

        listAirportsToRemove.clear();
        listAirportsFrom.clear();
        listAirportsTo.clear();

        listAirportsToRemove.addAll(airports);
        listAirportsFrom.addAll(airports);
        listAirportsTo.addAll(airports);

        listFlights.clear();
        listFlights.addAll(flights);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // ERROR REPORTING

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error has occurred. Description below:");
        alert.setContentText(message);

        alert.showAndWait();
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // CONVERTERS FOR COMBOBOXES

    private class VertexAirportConverter extends StringConverter<Vertex<Airport>> {

        @Override
        public String toString(Vertex<Airport> vertex) {
            if(vertex == null) return "";
            return vertex.element().toString();
        }

        @Override
        public Vertex<Airport> fromString(String string) {
            return null; // not needed
        }
    }

    private class EgdeFlightConverter extends StringConverter<Edge<Flight, Airport>> {

        @Override
        public String toString(Edge<Flight, Airport> edge) {
            if(edge == null) return "";
            return edge.element().toString();
        }

        @Override
        public Edge<Flight, Airport> fromString(String string) {
            return null; // not needed
        }
    }

}
