package preprocessor.file;

import heuristic.rpl.HeuristicRPL;
import heuristic.rpl.Solution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javaff.data.GroundProblem;
import javaff.data.UngroundProblem;
import javaff.data.strips.STRIPSInstantAction;
import javaff.parser.PDDL21parser;
import javaff.planning.TemporalMetricState;

public class GenerateExample {
	public static void generate(String domainPath, String examplePathPrefix,
			long numberExamples, String solutionFFPathPrefix,
			String solutionFFPathSufix, String resultDir) throws IOException {
		File domainFile = new File(domainPath);
		File problemFile = null;
		GroundProblem ground = null;
		TemporalMetricState atualState = null;
		Solution solution = null;
		long countAction = 0;
		String fileSolution = null;
		PrintWriter printWriter = null;
		List<String> plan = null;
		Iterator<STRIPSInstantAction> iterator = null;
		STRIPSInstantAction stripsInstantAction = null;
		String[] action = null;
		boolean found = false;
		String pathSolutionFF = null;
		try {
			for (int index = 1; index <= numberExamples; index++) {
				pathSolutionFF = solutionFFPathPrefix + index
						+ solutionFFPathSufix;
				if (existSolutionFF(pathSolutionFF)) {
					problemFile = new File(examplePathPrefix + index);

					UngroundProblem unground = PDDL21parser.parseFiles(
							domainFile, problemFile);
					if (unground == null) {
						System.out
								.println("Parsing error - see console for details");
					} else {
						ground = unground.ground();
						atualState = ground.getTemporalMetricInitialState();
						countAction = 0;
						calculateRPL(solution, atualState, fileSolution,
								resultDir, index, countAction, printWriter);
						plan = PlanExample.getPlan(pathSolutionFF);
						found = false;
						for (int indexAction = 0; indexAction < plan.size(); indexAction++) {
							iterator = atualState.getActions().iterator();
							action = plan.get(indexAction).split(" ");
							found = false;
							while (iterator.hasNext()) {
								stripsInstantAction = iterator.next();
								if (stripsInstantAction.name.toString()
										.equalsIgnoreCase(action[0])) {
									found = true;
									for (int indexActionParam = 1; indexActionParam < action.length; indexActionParam++) {
										if (!stripsInstantAction.params
												.get(indexActionParam - 1)
												.toString()
												.equalsIgnoreCase(
														action[indexActionParam])) {
											found = false;
											break;
										}
									}
									if (found) {
										countAction++;
										atualState = (TemporalMetricState) atualState
												.apply(stripsInstantAction);
										calculateRPL(solution, atualState,
												fileSolution, resultDir, index,
												countAction, printWriter);
										break;
									}
								}
							}
							if (!found) {
								System.out.println("Action Error: "
										+ plan.get(indexAction)
										+ " at Example " + index);
								countAction++;
							}
						}
					}
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
	}

	private static String getPathSolution(String resultDir, int indexFile,
			long countAction) {
		return resultDir + File.separator + "pfile" + indexFile + "SolutionRPL"
				+ countAction + ".pddl";
	}

	private static void calculateRPL(Solution solution,
			TemporalMetricState atualState, String fileSolution,
			String resultDir, int index, long countAction,
			PrintWriter printWriter) throws IOException {
		try {
			solution = HeuristicRPL.calculate(atualState);
			fileSolution = getPathSolution(resultDir, index, countAction);
			FileManager.write(fileSolution, solution.getValue().toString(),
					true);
			printWriter = new PrintWriter(new FileWriter(fileSolution, true));
			solution.getRelaxedPlan().print(printWriter);

		} catch (IOException e) {
			throw e;
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
	}

	private static boolean existSolutionFF(String path) {
		return new File(path).exists();
	}
}
