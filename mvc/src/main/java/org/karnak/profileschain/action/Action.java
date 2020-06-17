package org.karnak.profileschain.action;

import org.dcm4che6.data.DicomElement;
import org.dcm4che6.data.DicomObject;
import org.dcm4che6.data.VR;
import org.karnak.data.AppConfig;

import java.util.Optional;

public enum Action implements ActionStrategy {

    REPLACE("D", (dcm, tag, pseudo, dummy) -> dummy),

    DEFAULT_DUMMY("DDum", (dcm, tag, pseudo, dummy) -> {
        final String tagValue = dcm.getString(tag).orElse(null);
        final Optional<DicomElement> dcmItem = dcm.get(tag);
        final DicomElement dcmEl = dcmItem.get();
        final VR vr = dcmEl.vr();
        String defaultDummyValue = switch (vr) {
            case AE, CS, LO, LT, PN, SH, ST, UN, UT, UC, UR -> "UNKNOWN";
            case DS, FL, FD, IS, SL, SS, UL, US -> "0";
            case AS -> "045Y";
            case DA -> "19991111";
            case DT -> "19991111111111";
            case TM -> "111111";
            case UI -> AppConfig.getInstance().getHmac().uidHash(pseudo, tagValue);
            default -> null;
        };
        return REPLACE.execute(dcm, tag, pseudo, defaultDummyValue);
    }),

    KEEP("K", (dcm, tag, pseudo, dummy) -> {
        String keepValue = dcm.getString(tag).orElse(null);
        return keepValue;
    }),

    REMOVE("X", (dcm, tag, pseudo, dummy) -> null),

    REPLACE_NULL("Z", (dcm, tag, pseudo, dummy) -> null),

    UID("U", (dcm, tag, pseudo, dummy) -> {
        String uidValue = dcm.getString(tag).orElse(null);
        String uidHashed = AppConfig.getInstance().getHmac().uidHash(pseudo, uidValue);
        return uidHashed;
    });

    private final String symbol;
    private final ActionStrategy action;

    Action(String symbol, ActionStrategy action) {
        this.symbol = symbol;
        this.action = action;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String execute(DicomObject dcm, int tag, String pseudo, String dummy) {
        return action.execute(dcm, tag, pseudo, dummy);
    }
}
