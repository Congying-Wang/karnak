package org.karnak.profileschain.profiles;

import org.dcm4che6.data.DicomElement;
import org.dcm4che6.util.TagUtils;
import org.karnak.profileschain.action.Action;

public class KeepAllTags extends AbstractProfileItem {
    public KeepAllTags(String name, String codeName, ProfileItem parentProfile) {
        super(name, codeName, parentProfile);
    }

    @Override
    public Boolean isKeep(DicomElement dcmElem) {
        return true;
    }

    @Override
    public Action getAction(DicomElement dcmElem) {
        return Action.KEEP;
    }
}
