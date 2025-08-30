package org.mule.extension.zt.internal;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

public enum DBLErrorProvider implements ErrorTypeDefinition<DBLErrorProvider> {
      INVALID_PARAMETER,
      TIME_OUT,
      NOT_ALLOWED,
      DATACRYPT_ERROR
}