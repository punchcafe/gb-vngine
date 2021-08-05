package dev.punchcafe.vngine.gb.codegen.csan;

public class NarrativeName {

    public static String narrativeName(final String narrativeId){
        return String.format("nar_%s", narrativeId);
    }

    public static String elementName(final String narrativeId, final int elementIndex){
        return String.format("nar_%s_elem_%d", narrativeId, elementIndex);
    }

    public static String elementArrayName(final String narrativeId){
        return String.format("nar_%s_elems", narrativeId);
    }

    public static String elementBodyName(final String narrativeId, final int elementIndex){
        return String.format("nar_%s_elem_%d_body", narrativeId, elementIndex);
    }
}
