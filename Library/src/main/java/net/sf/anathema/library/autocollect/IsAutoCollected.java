package net.sf.anathema.library.autocollect;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface IsAutoCollected {
  // Tags Interfaces that are automatically collected via reflection
}