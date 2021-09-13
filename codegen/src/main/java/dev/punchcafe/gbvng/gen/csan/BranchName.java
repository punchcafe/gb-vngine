package dev.punchcafe.gbvng.gen.csan;

public class BranchName {

    public static String gameOverBranchName(final String nodeId){
        return String.format("%s_game_over_branch", NodeIdSanitiser.sanitiseNodeId(nodeId));
    }

    public static String numberedBranchName(final String nodeId, final int number){
        return String.format("%s_branch_%d", NodeIdSanitiser.sanitiseNodeId(nodeId), number);
    }

    public static String nodesBranchArrayName(final String nodeId){
        return String.format("%s_branches", NodeIdSanitiser.sanitiseNodeId(nodeId));
    }
}
