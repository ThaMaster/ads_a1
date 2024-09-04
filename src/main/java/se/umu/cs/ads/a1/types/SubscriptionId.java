package se.umu.cs.ads.a1.types;

import se.umu.cs.ads.a1.adts.AbstractStringType;
import se.umu.cs.ads.a1.util.Util;

import java.util.UUID;

public class SubscriptionId extends AbstractStringType
{
  //----------------------------------------------------------
  public SubscriptionId (String value)
  {
    super(value);

    if (!Util.validateId(value))
      throw new IllegalArgumentException(value);
  }


  //----------------------------------------------------------
  //----------------------------------------------------------
  public static SubscriptionId construct ()
  {
    String value = UUID.randomUUID().toString();
    return new SubscriptionId(value);
  }
}
