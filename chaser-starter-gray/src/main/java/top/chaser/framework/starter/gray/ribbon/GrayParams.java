package top.chaser.framework.starter.gray.ribbon;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class GrayParams {
    private Map<String, String> headers = new HashMap<>();
    private Map<String, Object> params = new HashMap();
}