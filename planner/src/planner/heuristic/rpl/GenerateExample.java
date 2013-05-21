package planner.heuristic.rpl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import planner.javaff.data.GroundProblem;
import planner.javaff.data.UngroundProblem;
import planner.javaff.data.strips.STRIPSInstantAction;
import planner.javaff.parser.PDDL21parser;
import planner.javaff.planning.TemporalMetricState;

public class GenerateExample {
	public static void generateAllExample(final String domainPath,
			final String examplePathPrefix, final long numberExamples,
			final String solutionFFPathPrefix,
			final String solutionFFPathSufix, final String resultDir)
			throws IOException {
		final File domainFile = new File(domainPath);
		final Solution solution = null;
		final String fileSolution = null;
		String pathSolutionFF = null;
		try {
			for (int index = 1; index <= numberExamples; index++) {
				pathSolutionFF = solutionFFPathPrefix + index
						+ solutionFFPathSufix;
				if (GenerateExampleUtils.existSolutionFF(pathSolutionFF)) {
					processExample(examplePathPrefix, resultDir, domainFile,
							solution, fileSolution, pathSolutionFF, index);
				}
			}
		} catch (final IOException e) {
			throw e;
		}
	}

	private static void generateExample(Solution solution,
			final TemporalMetricState currentState, String fileSolution,
			final String resultDir, final int index, final long countAction,
			final int distanceToGoal) throws IOException {
		fileSolution = getPathSolution(resultDir, index, countAction);
		solution = GenerateExampleUtils.calculateRPL(solution, currentState,
				fileSolution);
		GenerateExampleUtils.printValueToEstimate(solution, fileSolution,
				distanceToGoal);
		GenerateExampleUtils.printStatesFactsInS(currentState, fileSolution);
		GenerateExampleUtils.printAddList(solution, fileSolution);
		GenerateExampleUtils.printDeleteList(solution, fileSolution);
		GenerateExampleUtils.printGoal(currentState, fileSolution);
		GenerateExampleUtils.printCurrentEqualsGoal(currentState, fileSolution);
	}

	private static String getPathSolution(final String resultDir,
			final int indexFile, final long countAction) {
		return resultDir + File.separator + "pfile" + indexFile + "SolutionRPL"
				+ countAction + ".pddl";
	}

	@SuppressWarnings("unchecked")
	private static void processExample(final String examplePathPrefix,
			final String resultDir, final File domainFile,
			final Solution solution, final String fileSolution,
			final String pathSolutionFF, final int index) throws IOException {
		File problemFile = null;
		GroundProblem ground = null;
		TemporalMetricState currentState;
		long countAction = 1;
		List<String> plan = null;
		Iterator<STRIPSInstantAction> iterator = null;
		STRIPSInstantAction stripsInstantAction = null;
		String[] action = null;
		boolean found = false;
		problemFile = new File(examplePathPrefix + index);

		final UngroundProblem unground = PDDL21parser.parseFiles(domainFile,
				problemFile);

		int distanceToGoal = 0;
		if (unground == null) {
			System.out.println("Parsing error - see console for details");
		} else {
			ground = unground.ground(null);
			currentState = ground.getTemporalMetricInitialState();
			countAction = 1;
			plan = PlanExample.getPlan(pathSolutionFF);
			distanceToGoal = plan.size();
			generateExample(solution, currentState, fileSolution, resultDir,
					index, countAction, distanceToGoal);
			found = false;
			for (int indexAction = 0; indexAction < (plan.size() - 1); indexAction++) {
				iterator = currentState.getActions().iterator();
				action = plan.get(indexAction).split(" ");
				found = false;
				while (iterator.hasNext()) {
					stripsInstantAction = iterator.next();
					if (stripsInstantAction.name.toString().equalsIgnoreCase(
							action[0])) {
						found = true;
						for (int indexActionParam = 1; indexActionParam < action.length; indexActionParam++) {
							if (!stripsInstantAction.params
									.get(indexActionParam - 1).toString()
									.equalsIgnoreCase(action[indexActionParam])) {
								found = false;
								break;
							}
						}
						if (found) {
							countAction++;
							distanceToGoal--;
							currentState = (TemporalMetricState) currentState
									.apply(stripsInstantAction);
							generateExample(solution, currentState,
									fileSolution, resultDir, index,
									countAction, distanceToGoal);
							break;
						}
					}
				}
				if (!found) {
					System.out.println("Action Error: " + plan.get(indexAction)
							+ " at Example " + index);
					countAction++;
				}
			}
		}
	}
}
