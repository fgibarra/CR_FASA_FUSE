package cl.ahumada.proxy.tcp.coberturaPeyaPos;

import proxy.tcp.lib.ProxyMainBase;
import proxy.tcp.lib.threads.ProcesoRequestClienteImpl;
import proxy.tcp.lib.utils.Utils;

public class ProxyMain extends ProxyMainBase {

    protected final String log4jConfigFile = "resources/proxyws_log4j.properties";
    private static String properyFileName = "resources/proxy_coberturapeya.properties";
    private final String portPropertyName = "proxy.tcp.coberturapeya.port";
    private final String endpointPropertyName = "proxy.tcp.coberturapeya.endpoint";
    private final String version = "CoberturaPEYA-1.0.0";
    public static ProxyMain instance;

    /**
     * @return the instance
     */
    public static ProxyMain getInstance() {
        return instance;
    }

    public static void main( String[] args )
    {
        System.out.println("partimos");
        instance = new ProxyMain();
        instance.setProps( instance.initFromProperties(properyFileName));
        instance.logger.info(String.format("Proxy parte: %s", Utils.getInstance(
        		).dumpProperties(instance.getProps())));
        instance.start();
    }

    @Override
    public Class<? extends ProcesoRequestClienteImpl> getClaseProcesaRequestCliente() {
        return CoberturaPeyaApirest.class;
    }

    @Override
    public String getEndpointPropertyName() {
        return endpointPropertyName;
    }

    @Override
    public String getLog4ConfigFile() {
        return log4jConfigFile;
    }

    @Override
    public String getPortPropertyName() {
        return portPropertyName;
    }

    @Override
    public Integer getProxyPort() {
        return props != null && props.getProperty(getPortPropertyName())!= null ? Integer.valueOf(props.getProperty(getPortPropertyName())) : null;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public boolean isInicializado() {
        boolean validacion = false;
        if (getProps() != null) {
            if (getListenPort() != null) {
                if (getEndpoint() != null) {
                    validacion = true;
                }
            }
        }
        return validacion;
     }
    
}
