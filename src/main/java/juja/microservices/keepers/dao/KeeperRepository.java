package juja.microservices.keepers.dao;

import juja.microservices.keepers.entity.Keeper;

import java.util.List;

public interface KeeperRepository {
    List<Keeper> getAllActiveKeepers();
}