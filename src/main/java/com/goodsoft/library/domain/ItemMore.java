package com.goodsoft.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Class for work with item more info
 *
 * @author Vjacheslav Filin
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ItemMore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * game name
     */
    private String name;

    /**
     * Ru name
     */
    private String ruName;

    /**
     * game price
     */
    private double price;

    /**
     * Is item or gae
     */
    private boolean isItem;

    /**
     * Was loaded system
     */
    private boolean createdSystem = false;

    private boolean wasBuy = true;

    public ItemMore(Item item) {
        setItem(false);
        setName(item.getMarketHashName().replace("Prismatic: ", "").replace("Ethereal: ", ""));
        setPrice(item.getPrice());
        setCreatedSystem(true);
        setWasBuy(true);
        setId(item.getId());
    }
}
