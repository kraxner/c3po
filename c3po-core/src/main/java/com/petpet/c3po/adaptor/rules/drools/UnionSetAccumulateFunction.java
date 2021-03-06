package com.petpet.c3po.adaptor.rules.drools;

import java.io.Serializable;
import java.util.Collection;

import org.drools.base.accumulators.CollectSetAccumulateFunction;

/**
 * <p>
 * This is a custom drools accumulation function, that allow to union sets of
 * objects into one set. It simply calls the
 * {@link CollectSetAccumulateFunction} on every single element.
 * </p>
 * 
 * <p>
 * It needs to be defined in the drools.packagebuilder.conf configuration file.
 * </p>
 */
public class UnionSetAccumulateFunction extends CollectSetAccumulateFunction {

  @Override
  public void accumulate(Serializable context, Object value) {
    if (value instanceof Collection<?>) {
      for (Object item : (Collection<?>) value) {
        super.accumulate(context, item);
      }
    }
  }

  @Override
  public void reverse(Serializable context, Object value) throws Exception {
    if (value instanceof Collection<?>) {
      for (Object item : (Collection<?>) value) {
        super.reverse(context, item);
      }
    }
  }
}
