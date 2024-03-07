package com.github.hummel.dc.lab2.service.impl

import com.github.hummel.dc.lab2.bean.Sticker
import com.github.hummel.dc.lab2.dto.request.StickerRequestTo
import com.github.hummel.dc.lab2.dto.request.StickerRequestToId
import com.github.hummel.dc.lab2.dto.response.StickerResponseTo
import com.github.hummel.dc.lab2.service.StickerService
import com.github.hummel.dc.lab2.util.BaseRepository

class StickerServiceImpl(
	private val messageRepository: BaseRepository<Sticker, Long>
) : StickerService {
	override fun getAll(): List<StickerResponseTo> {
		val result = messageRepository.data.map { it.second }

		return result.map { it.toResponse() }
	}

	override fun create(messageRequestTo: StickerRequestTo?): StickerResponseTo? {
		val id = if (messageRepository.data.isEmpty()) {
			-1
		} else {
			messageRepository.getLastItem()?.id ?: return null
		} + 1

		val bean = messageRequestTo?.toBean(id) ?: return null
		val result = messageRepository.addItem(bean.id, bean)

		return result?.toResponse()
	}

	override fun deleteById(id: Long): Boolean = messageRepository.removeItem(id)

	override fun getById(id: Long): StickerResponseTo? {
		val result = messageRepository.getItemById(id)?.second

		return result?.toResponse()
	}

	override fun update(messageRequestToId: StickerRequestToId?): StickerResponseTo? {
		val bean = messageRequestToId?.toBean() ?: return null
		val result = messageRepository.addItem(bean.id, bean)

		return result?.toResponse()
	}
}