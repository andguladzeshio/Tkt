package com.example.tkt.activity.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.tkt.databinding.LayoutTicketItemBinding
import com.example.tkt.model.Ticket
import com.example.tkt.util.setQR
import com.example.tkt.util.toFormattedDate

class TicketRecyclerAdapter(
    private val shouldShowBuyButton: Boolean,
    private val buyTicket: (Ticket)-> Unit = {}
): RecyclerView.Adapter<TicketRecyclerAdapter.ViewHolder>() {
    private var tickets = listOf<Ticket>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(tickets: List<Ticket>) {
        this.tickets = tickets
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutTicketItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = tickets.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(tickets[position], shouldShowBuyButton, buyTicket)
    }

    class ViewHolder(private val binding: LayoutTicketItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(ticket: Ticket, shouldShowBuyButton: Boolean, buyTicket: (Ticket) -> Unit) = with(binding) {
            buyNowButton.isVisible = shouldShowBuyButton

            if (shouldShowBuyButton) {
                buyNowButton.setOnClickListener { buyTicket(ticket) }

                val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(24))
                Glide.with(QRCodeImageView)
                    .load("https://picsum.photos/500/500")
                    .centerCrop()
                    .apply(requestOptions)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(QRCodeImageView)
            } else QRCodeImageView.setQR(ticket.promoCode)

            eventNameTextView.text = ticket.name
            eventPlaceTextView.text = ticket.place
            eventTimeTextView.text = ticket.time.toFormattedDate()
        }

    }
}