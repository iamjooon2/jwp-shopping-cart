package cart.service;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import cart.dto.AuthDto;
import cart.dto.request.CreateMemberRequest;
import cart.dto.response.MemberResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberDao memberDao;

    @Autowired
    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        final Optional<List<MemberEntity>> memberEntitiesOptional = memberDao.findAll();
        if (memberEntitiesOptional.isEmpty()) {
            return new ArrayList<>();
        }
        return memberEntitiesOptional.get().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public MemberEntity findMember(final AuthDto authDto) {
        final Member member = new Member(authDto.getEmail(), authDto.getPassword());
        final Optional<MemberEntity> memberResult = memberDao.findMember(member);
        if (memberResult.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 회원 토큰입니다.");
        }
        return memberResult.get();
    }

    @Transactional
    public void create(final CreateMemberRequest createMemberRequest) {
        final Member member = createMemberRequest.toMember();
        final Optional<MemberEntity> memberResult = memberDao.findMemberByEmail(member.getEmail());
        if (memberResult.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        memberDao.insert(member);
    }
}
