package org.mule.extension.zt.internal;

import java.util.HashSet;
import java.util.Set;

import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;


public class DBLExecuteErrorTypeProvider implements ErrorTypeProvider {
    @Override
    public Set<ErrorTypeDefinition> getErrorTypes() {
        HashSet<ErrorTypeDefinition> errors = new HashSet<>();
        errors.add(DBLErrorProvider.INVALID_PARAMETER);
        errors.add(DBLErrorProvider.TIME_OUT);
        errors.add(DBLErrorProvider.NOT_ALLOWED);
        return errors;
    }
}