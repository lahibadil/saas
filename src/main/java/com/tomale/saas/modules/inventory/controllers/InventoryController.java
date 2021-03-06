package com.tomale.saas.modules.inventory.controllers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/inventory")
public class InventoryController {

    public final static Logger log = LogManager.getLogger(InventoryController.class);

    @GetMapping("")
    @PreAuthorize("hasAuthority('inventory.dashboard')")
    public String viewDefault() {
        log.debug("VIEW: inventory.default");
        return "inventory/default";
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('inventory.dashboard')")
    public String viewDashboard() {
        log.debug("VIEW: inventory.dashboard");
        return "inventory/default";
    }
}