package org.karnak.profileschain.action;

import org.dcm4che6.data.DicomObject;
import java.util.Iterator;
import org.dcm4che6.data.DicomElement;

public interface Action {
    void execute(DicomObject attributes, int tag, Iterator<DicomElement> iterator, String pseudonym, String dummyValue);

    String getStrAction();
}