package org.renjin.primitives.packaging;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.renjin.eval.Context;
import org.renjin.eval.EvalException;
import org.renjin.primitives.S3;
import org.renjin.primitives.annotations.SessionScoped;
import org.renjin.primitives.packaging.NamespaceDef.S3Export;
import org.renjin.sexp.Closure;
import org.renjin.sexp.Environment;
import org.renjin.sexp.Function;
import org.renjin.sexp.NamedValue;
import org.renjin.sexp.PrimitiveFunction;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.Symbol;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Session-level registry of namespaces
 *
 */
@SessionScoped
public class NamespaceRegistry {

  private static final Symbol BASE = Symbol.get("base");
  
  private PackageLoader loader;
	private Map<Symbol, Namespace> namespaces = Maps.newIdentityHashMap();
	private Map<Environment, Namespace> envirMap = Maps.newIdentityHashMap();
	
  private Context context;
	
	public NamespaceRegistry(PackageLoader loader, Context context, Environment baseNamespaceEnv) {
	  this.loader = loader;
	  this.context = context;
	  Namespace baseNamespace = new Namespace(new NamespaceDef(), "base", baseNamespaceEnv);
    namespaces.put(BASE, baseNamespace);
	  envirMap.put(baseNamespaceEnv, baseNamespace);
	}

	public Namespace getBaseNamespace() {
	  return namespaces.get(BASE);
	}
	
	public Environment getBaseNamespaceEnv() {
	  return getBaseNamespace().getNamespaceEnvironment();
	}
	
	public Namespace getNamespace(Environment envir) {
	  Namespace ns = envirMap.get(envir);
	  if(ns == null) {
	    throw new IllegalArgumentException();
	  }
	  return ns;
	}
	
  public Namespace getNamespace(Symbol name) {
	  Namespace namespace = namespaces.get(name);
	  if(namespace == null) {
	    try {
        namespace = load(name);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
	    namespaces.put(name, namespace);
	  }
	  return namespace;
	}

  public Namespace getNamespace(String name) {
    return getNamespace(Symbol.get(name));
  }
  
  private Namespace load(Symbol name) throws IOException {

    Package pkg = loader.load(name.getPrintName());
    
    // load the serialized functions/values from the classpath
    // and add them to our private namespace environment
    Namespace namespace = createNamespace(pkg.getNamespaceDef(), name.getPrintName());
    for(NamedValue value : pkg.loadSymbols(context)) {
      namespace.getNamespaceEnvironment().setVariable(Symbol.get(value.getName()), value.getValue());
    }

    Set<Symbol> groups = Sets.newHashSet(Symbol.get("Ops"));
    
    // we need to export S3 methods to the namespaces to which
    // the generic functions were defined
    for(S3Export export : namespace.getDef().getS3Exports()) {
      Function method = (Function) namespace.getNamespaceEnvironment().getVariable(export.getMethod());
      Environment definitionEnv;
      if(groups.contains(export.getGenericFunction())) {
        definitionEnv = getBaseNamespaceEnv();
      } else {
        SEXP genericFunction = namespace.getNamespaceEnvironment().findFunction(context, export.getGenericFunction());
        if(genericFunction == null) {
          //throw new EvalException("Cannot find S3 method definition '" + export.getGenericFunction() + "'");
          System.err.println("Cannot find S3 method definition '" + export.getGenericFunction() + "'");
          continue;
        }
        definitionEnv = getDefinitionEnv( genericFunction );
      }
      if(!definitionEnv.hasVariable(S3.METHODS_TABLE)) {
        definitionEnv.setVariable(S3.METHODS_TABLE, Environment.createChildEnvironment(context.getBaseEnvironment()));
      }
      Environment methodsTable = (Environment) definitionEnv.getVariable(S3.METHODS_TABLE);
      methodsTable.setVariable(export.getMethod(), method);
      //System.out.println("installing " + export.getMethod() + " to " + definitionEnv.getName());
    }
    
    return namespace;
  }
  
  private Environment getDefinitionEnv(SEXP genericFunction) {
    if(genericFunction instanceof Closure) {
      return ((Closure) genericFunction).getEnclosingEnvironment();
    } else if(genericFunction instanceof PrimitiveFunction) {
      return getBaseNamespaceEnv();
    } else {
      throw new IllegalArgumentException(genericFunction.getClass().getName());
    }
  }

  public boolean isRegistered(Symbol name) {
    return namespaces.containsKey(name);
  }
  
  public Namespace getBase() {
    return namespaces.get(BASE);
  }

  /**
   * Creates a new empty namespace 
   * @param namespaceDef 
   * @param namespaceName
   * @return
   */
  public Namespace createNamespace(NamespaceDef namespaceDef, String namespaceName) {
    // each namespace has environment which is the leaf in a hierarchy that
    // looks like this:
    // BASE-NS -> IMPORTS -> ENVIRONMENT
    
    Environment imports = Environment.createNamedEnvironment(getBaseNamespaceEnv(), "imports:" + namespaceName);
    Environment namespaceEnv = Environment.createNamedEnvironment(imports, "namespace:" + namespaceName);
    Namespace namespace = new Namespace(namespaceDef, namespaceName, namespaceEnv);
    namespaces.put(Symbol.get(namespaceName), namespace);
    envirMap.put(namespaceEnv, namespace);
    return namespace;
  }

  public boolean isNamespaceEnv(Environment envir) {
    return envirMap.containsKey(envir);
  }
}