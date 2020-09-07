package top.chaser.framework.cloud.starter.gray.ribbon;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import top.chaser.framework.cloud.starter.gray.WeightMeta;
import top.chaser.framework.cloud.starter.gray.WeightRandomUtils;
import top.chaser.framework.cloud.starter.gray.autoconfigure.GrayProperties;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.base.util.SpringBeanHelper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class GrayRule extends ZoneAvoidanceRule {

    public Server choose(List<Server> servers) {
        Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(servers);
        if (server.isPresent()) {
            return server.get();
        } else {
            return null;
        }
    }

    @Override
    public Server choose(Object key) {
        GrayProperties grayProperties = SpringBeanHelper.getBean(GrayProperties.class);
        Map<String, GrayProperties.Strategy> strategys = new HashMap<>();
        String clientName = null;

        for (GrayProperties.Strategy strategy : grayProperties.getStrategys()) {
            strategys.put(strategy.getServiceName(), strategy);
        }

        ILoadBalancer loadBalancer = getLoadBalancer();
        if (loadBalancer instanceof ZoneAwareLoadBalancer) {
            clientName = ((ZoneAwareLoadBalancer) loadBalancer).getClientConfig().getClientName();
            if (!strategys.containsKey(clientName)) {
                return super.choose(key);
            }
        } else {
            return super.choose(key);
        }

        GrayProperties.Strategy strategy = strategys.get(clientName);

        List<Server> allServers = loadBalancer.getAllServers();
        List<Server> reachableServers = loadBalancer.getReachableServers();

        if (allServers.size() == 0 || reachableServers.size() == 0) {
            return super.choose(key);
        }

        List<Server> grayReachableServers = new ArrayList<>();
        List<Server> generalReachableServers = new ArrayList<>();


        for (Server instance : reachableServers) {
            if (instance.getClass().getName().indexOf("NacosServer") != -1) {
                Map<String, String> metadata = ReflectUtil.invoke(instance, "getMetadata");
                String version = metadata.get("version");

                if (strategy.getVersion().trim().equals(version.trim())) {
                    grayReachableServers.add(instance);
                } else {
                    generalReachableServers.add(instance);
                }
            } else {
                return super.choose(key);
            }
        }

        List<Server> callServers = null;
        if (grayReachableServers.size() > 0 && match(strategy, key)) {
            Map<List<Server>, Integer> weightMap = new HashMap<>();
            weightMap.put(grayReachableServers, strategy.getPercentage());
            weightMap.put(generalReachableServers, 100 - strategy.getPercentage());
            WeightMeta<List<Server>> nodes = WeightRandomUtils.buildWeightMeta(weightMap);
            callServers = nodes.random();
            if(log.isDebugEnabled()){
                log.debug("gray to:{}", JSONUtil.toJSONString(callServers.stream().map(server -> strategy.getServiceName()+":"+server.getId()+"."+strategy.getVersion()).collect(Collectors.toList())));
            }
        } else {
            callServers = generalReachableServers;
            if(log.isDebugEnabled()) {
                log.debug("general to:{}", JSONUtil.toJSONString(callServers.stream().map(server -> strategy.getServiceName() + ":" + server.getId() + "." + strategy.getVersion()).collect(Collectors.toList())));
            }
        }

        return choose(callServers);
    }

    private boolean match(GrayProperties.Strategy strategy, Object key) {
        boolean flag = true;
        Set<GrayProperties.Condition> conditions = strategy.getConditions();
        if (key != null) {
            condition:
            for (GrayProperties.Condition condition : conditions) {
                type:
                switch (condition.getType()) {
                    case PARAM:
                        boolean bodyFlag = matchBody(condition, key);
                        if (bodyFlag) {
                            break type;
                        } else {
                            flag = false;
                            break condition;
                        }
                    case HEADER:
                        boolean headerFlag = matchHeader(condition, key);
                        if (headerFlag) {
                            break type;
                        } else {
                            flag = false;
                            break condition;
                        }
                    default:
                        flag = false;
                        break condition;
                }
            }
        } else {
            flag = false;
        }
        return flag;
    }

    private boolean matchBody(GrayProperties.Condition condition, Object key) {
        boolean flag = false;
        if(key instanceof GrayParams){
            Map<String, Object> params = ((GrayParams) key).getParams();
            String paramsValue = JSONUtil.getByPath(params, condition.getName(), String.class);
            if(log.isDebugEnabled()) {
                log.debug("condition:{},params:{}", JSONUtil.toJSONString(condition), paramsValue);
            }
            if(!StrUtil.isEmpty(paramsValue)) {
                if (paramsValue.equals(condition.getValue())) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private boolean matchHeader(GrayProperties.Condition condition, Object key) {
        boolean flag = false;
        if(key instanceof GrayParams){
            Map<String, String> headers = ((GrayParams) key).getHeaders();
            String headerValue = headers.get(condition.getName());
            if(!StrUtil.isEmpty(headerValue)){
                if(headerValue.equals(condition.getValue())){
                    flag = true;
                }
            }
        }
        return flag;
    }
}
