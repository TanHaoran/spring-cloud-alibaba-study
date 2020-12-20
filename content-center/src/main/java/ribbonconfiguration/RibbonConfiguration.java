package ribbonconfiguration;

import com.jerry.contentcenter.configuration.NacosSameClusterWeightedRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA
 * User: Jerry
 * Date: 2020/12/20
 * Time: 14:48
 * Description:
 */
@Configuration
public class RibbonConfiguration {

    @Bean
    public IRule ribbonRule() {
        return new NacosSameClusterWeightedRule();
    }

}
