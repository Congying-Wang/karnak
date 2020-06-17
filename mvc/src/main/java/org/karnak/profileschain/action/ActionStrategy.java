package org.karnak.profileschain.action;

import org.dcm4che6.data.DicomObject;
import org.karnak.profileschain.ProfileChain;

@FunctionalInterface
public interface ActionStrategy {
    String execute(DicomObject dcm, int tag, String pseudo, String dummy);
}