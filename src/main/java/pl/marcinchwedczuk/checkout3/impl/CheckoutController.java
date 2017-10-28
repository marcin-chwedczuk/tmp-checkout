package pl.marcinchwedczuk.checkout3;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {


    @RequestMapping(value = "/heartbeat", method = GET)
    public String heartbeat() {
        return "Works!";
    }
}
