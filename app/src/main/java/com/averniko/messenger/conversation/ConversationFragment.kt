package com.averniko.messenger.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.averniko.messenger.R
import com.averniko.messenger.data.login.LoginDataSource
import com.averniko.messenger.data.login.LoginRepository
import com.averniko.messenger.data.model.Message
import com.averniko.messenger.databinding.FragmentConversationBinding


class ConversationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loginRepository: LoginRepository = LoginRepository.getInstance(LoginDataSource())
        val binding = DataBindingUtil.inflate<FragmentConversationBinding>(
            inflater,
            R.layout.fragment_conversation,
            container,
            false
        )
        val args = ConversationFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = ConversationViewModelFactory(args.interlocutor)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ConversationViewModel::class.java)

        val adapter = ConversationAdapter()
        binding.messagesList.adapter = adapter

        viewModel.conversations.observe(viewLifecycleOwner, Observer { conversations ->
            if (conversations != null) {
                val conversation = conversations.find { conversation ->
                    conversation.interlocutor == args.interlocutor
                }
                if (conversation != null)
                    adapter.data = conversation.messages
                binding.messagesList.smoothScrollToPosition(adapter.itemCount)
            }
        })

        binding.sendMessageButton.setOnClickListener {
            val login: String? = loginRepository.user?.login
            if (login != null)
                viewModel.onSendMessage(
                    Message(
                        sender = login,
                        receiver = args.interlocutor,
                        text = binding.messageTextInput.text.toString(),
                        isSend = true
                    )
                )
            binding.messagesList.smoothScrollToPosition(adapter.itemCount)
        }

        return binding.root
    }
}