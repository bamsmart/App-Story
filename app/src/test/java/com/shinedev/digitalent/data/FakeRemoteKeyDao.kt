package com.shinedev.digitalent.data

import com.shinedev.digitalent.data.local.room.dao.RemoteKeysDao
import com.shinedev.digitalent.data.local.room.entity.RemoteKeys

class FakeRemoteKeyDao : RemoteKeysDao {

    private var remoteKeyData = mutableListOf<RemoteKeys>()

    override suspend fun insertAll(remoteKey: List<RemoteKeys>) {
        remoteKey.forEach {
            remoteKeyData.add(it)
        }
    }

    override suspend fun getRemoteKeysId(id: String): RemoteKeys {
        return remoteKeyData.first { it.id == id }
    }

    override suspend fun deleteRemoteKeys() {
        remoteKeyData.clear()
    }

}