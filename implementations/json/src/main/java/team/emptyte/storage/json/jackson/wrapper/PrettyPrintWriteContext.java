package team.emptyte.storage.json.jackson.wrapper;

import tools.jackson.core.ObjectWriteContext;
import tools.jackson.core.PrettyPrinter;
import tools.jackson.core.util.DefaultPrettyPrinter;

public class PrettyPrintWriteContext extends ObjectWriteContext.Base {
  private final PrettyPrinter prettyPrinter;

  public PrettyPrintWriteContext(final boolean prettyPrint) {
    this.prettyPrinter = prettyPrint ? new DefaultPrettyPrinter() : null;
  }

  @Override
  public PrettyPrinter getPrettyPrinter() {
    return this.prettyPrinter;
  }
}
