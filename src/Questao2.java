import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.GmlImporter;
import org.jgrapht.io.EdgeProvider;
import org.jgrapht.io.ImportException;
import org.jgrapht.io.VertexProvider;
import org.jgrapht.alg.cycle.HierholzerEulerianCycle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/** Importa Grafo Simples no formato GML, verifica se este possui um caminho euleriano,
 * se possuir, retornara a representaçao textual deste caminho.
 * @author Rafael Dantas 
 * @author Thayanne Sousa
 * @author Gabriel Guimarães
 * @author João Pedro
 */
public class Questao2 {

	public static void main(String[] args) {
		VertexProvider<Object> vp1 =
				DefaultVertex::new;

		EdgeProvider<Object, RelationshipEdge> ep1 =
				(from, to, label, attributes) -> new RelationshipEdge(from, to, attributes);

		GmlImporter<Object, RelationshipEdge> gmlImporter = new GmlImporter<>(vp1, ep1);

		Graph<Object, RelationshipEdge> graphgml = new SimpleGraph<>(RelationshipEdge.class);

		try {
			//Insira o caminho para o arquivo gml a ser importado
			gmlImporter.importGraph(graphgml, ImportGraph.readFile("/home/joaop/TeoriaGraf/rede.gml"));
		} catch (ImportException e) {
			throw new RuntimeException(e);
		}

		System.out.println(caminho(graphgml));
	}

	/**
	 * Verifica se este possui um caminho euleriano, se possuir, retornara a representaçao textual deste caminho.
	 * Possibilita ao usuário a escolha da máquina gerente.
	 * @param graphgml - grafo a ser verificado
	 * @return String represetação textual do caminho, se não poussir retorna uma mensagem
	 */
	private static String caminho(Graph<Object, RelationshipEdge> graphgml) {
		HierholzerEulerianCycle<Object, RelationshipEdge> graph = new HierholzerEulerianCycle<>();

		if (graph.isEulerian(graphgml)) {

			Scanner inp = new Scanner(System.in);
			System.out.print("Denominação da máquina gerente: ");
			String gerente = inp.nextLine().toLowerCase();

			//Conversão do caminho gerado para array e posteriormente para ArrayList, possibilitando modificação.
			Object[] path = graph.getEulerianCycle(graphgml).getVertexList().toArray();
			ArrayList<Object> list = new ArrayList<>(Arrays.asList(path));

			//Adiciona o passo no final e remove do inicio.
			for (int i = 1; i < list.size(); i++) {
				if (!path[i].toString().toLowerCase().equals(gerente)) {
					list.add(path[i]);
					list.remove(path[i]);
				} else break;
			}

			//Retirando o primeiro, repetido.
			list.remove(0);

			StringBuilder saida = new StringBuilder();
			for (Object aList : list) {
				saida.append(aList.toString()).append(" >> ");
			}

			return System.lineSeparator() + "Rota:" + System.lineSeparator() + saida;

		} else return "Não é possível encontrar uma rota neste grafo";
	}
}


