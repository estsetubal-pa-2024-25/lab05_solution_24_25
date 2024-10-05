import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SuppressWarnings("removal")
public class FirstProgram extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Graph<Character, Integer> g = new GraphEdgeList<>();

        // TODO: create graph structure
        Vertex<Character> vA = g.insertVertex('a');
        Vertex<Character> vB = g.insertVertex('b');
        Vertex<Character> vC = g.insertVertex('c');
        Vertex<Character> vD = g.insertVertex('d');
        Vertex<Character> vE = g.insertVertex('e');
        Vertex<Character> vF = g.insertVertex('f');
        Vertex<Character> vG = g.insertVertex('g');

        g.insertEdge(vA, vB, 6);
        g.insertEdge(vB, vC, 2);
        g.insertEdge(vC, vD, 30);
        g.insertEdge(vD, vE, 10);
        g.insertEdge(vD, vF, 22);
        g.insertEdge(vF, vE, 15);
        g.insertEdge(vF, vG, 8);
        g.insertEdge(vE, vG, 50);
        g.insertEdge(vG, vA, 11);

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<Character, Integer> graphView = new SmartGraphPanel<>(g, strategy);

        Scene scene = new Scene(new SmartGraphDemoContainer(graphView), 800, 800);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph Visualization");
        stage.setScene(scene);
        stage.show();

        graphView.init();

        // TODO: call implemented methods
    }

    // TODO: implement methods

    public static void main(String[] args) {
        launch(args);
    }
}
